package com.risevision.gcslogs.load;

import java.util.ArrayList;
import java.util.List;
import java.io.IOException;
import java.util.logging.Logger;
import com.google.appengine.api.taskqueue.*;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;
import com.google.api.services.bigquery.model.Job;
import com.risevision.gcslogs.BuildConfig;

class LogLoader {
  private static final int FILES_PER_BQ_LOAD_JOB =
  Integer.parseInt(BuildConfig.FILES_PER_BQ_LOAD_JOB.toString());
  private static final Logger log = Logger.getLogger("gcslogs.LogLoader");
  private static final Queue queue = QueueFactory.getQueue("storageLogDeletions");
  int loadCount = 0;

  LoadJobInserter bqLoadJobInserter;
  GCSLogsFetcher logsFetcher;

  LogLoader(GCSLogsFetcher logsFetcher, LoadJobInserter inserter) {
    this.logsFetcher = logsFetcher;
    this.bqLoadJobInserter = inserter;
  }

  void begin() {
    if (queue.fetchStatistics().getNumTasks() > 0) {
      log.warning("Deletion tasks pending - aborting loading of new logs.");
      return;
    }

    log.info("Beginning log load");

    logsFetcher.fetchLogs();

    loadLogs(logsFetcher.getUsageLogs(), "Usage");
    loadLogs(logsFetcher.getStorageLogs(), "Storage");

    log.info("Submitted load and delete jobs for " + loadCount + " filess.");
  }

  void loadLogs(List<String> logFileUris, String logType) {
    List<String> subList;
    int startIdxInclusive = 0;
    int endIdxExclusive = 0;

    while (startIdxInclusive < logFileUris.size()) {
      endIdxExclusive =
      Math.min(startIdxInclusive + FILES_PER_BQ_LOAD_JOB, logFileUris.size());

      subList = logFileUris.subList(startIdxInclusive, endIdxExclusive);
      submitLoadAndDeleteRequests(subList, logType);
      startIdxInclusive = startIdxInclusive + endIdxExclusive;
    }
  }

  void submitLoadAndDeleteRequests(List<String> fileUris, String logType) {
    this.loadCount += fileUris.size();
    String jobId = bqLoadJobInserter.insertJob(fileUris, logType)
    .getJobReference().getJobId();

    if (jobId == null) {return;}
    queue.add(withUrl("/deleteLoadJobFiles").countdownMillis(10_000L)
    .param("jobId", jobId));
  }
}

