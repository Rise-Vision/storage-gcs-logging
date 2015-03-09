package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.CredentialProvider;
import static com.risevision.gcslogs.BuildConfig.*;

import java.util.logging.Logger;
import java.util.*;
import java.io.IOException;

import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.services.storage.*;
import com.google.api.services.storage.model.*;
import com.google.api.services.bigquery.*;
import com.google.api.services.bigquery.model.*;
import com.google.api.client.googleapis.util.Utils;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

class DeleteLoadJobFilesServletHandler {
  private static final com.google.appengine.api.taskqueue.Queue queue =
  com.google.appengine.api.taskqueue.QueueFactory.getQueue("storageLogDeletions");

  static final int STATUS_OK = 200;
  static final int STATUS_PROCESSING = 102;
  private static final Logger log = Logger.getLogger("gcslogs.DeleteFilesHandler");

  String jobId;
  private int status;
  private Bigquery bq;
  private Storage storage;
  List<String> uris;

  DeleteLoadJobFilesServletHandler
  (Map<String, String[]> params, CredentialProvider credentialProvider) {
    this.status = STATUS_PROCESSING;

    bq = new Bigquery.Builder(Utils.getDefaultTransport(),
    Utils.getDefaultJsonFactory(), credentialProvider.getCredential())
    .setApplicationName(STORAGE_APP_NAME.toString()).build();

    storage = new Storage.Builder(Utils.getDefaultTransport(),
    Utils.getDefaultJsonFactory(), credentialProvider.getCredential())
    .setApplicationName(STORAGE_APP_NAME.toString()).build();

    if (params.containsKey("jobId")) {jobId = params.get("jobId")[0];}
    if (jobId == null) {
      log.severe("No job parameter specifid\n" + "Params: " + params.toString());
      throw new RuntimeException("No job to process.");
    }
  }

  void deleteFiles() {
    if (jobId != null) {getUrisFromBigqueryJob();}
    if (uris == null) { return; }
    log.info("Deleting " + uris.size() + " files from cloud storage log bucket.");
    deleteFilesFromCloudStorage();
  }

  void getUrisFromBigqueryJob() {
    Job job;
    try {
      job = bq.jobs().get(PROJECT_ID.toString(), jobId).execute();
    } catch (IOException e) {
      e.printStackTrace();
      this.status = STATUS_PROCESSING;
      return;
    }
 
    if (job.getStatus().getErrorResult() != null ||
    !job.getStatus().getState().equals("DONE")) {
      this.status = STATUS_PROCESSING;
      log.info("Job " + jobId + " has not completed.  Remaining in queue.");
      return;
    }

    uris = job.getConfiguration().getLoad().getSourceUris();
  }

  void deleteFilesFromCloudStorage() {
    String bucket;
    String object;

    try {
      BatchRequest batchRequest = storage.batch();
      for (String uri : uris) {
        bucket = extractBucketName(uri);
        object = extractObjectName(uri);

        storage.objects().delete(bucket, object )
       .queue(batchRequest, newCallback(bucket, object));
      }

      batchRequest.execute();
    } catch (IOException e) {
      log.severe(e.toString());
      e.printStackTrace();
      this.status = STATUS_PROCESSING;
      return;
    }

    this.status = STATUS_OK;
  }

  String extractObjectName(String uri) {
    int idx =  uri.indexOf("/", 5) + 1;
    return uri.substring(idx);
  }

  String extractBucketName(String uri) {
    int idx =  uri.indexOf("/", 5);
    return uri.substring(5, idx);
  }

  JsonBatchCallback<Void> newCallback(final String bucket, final String object) {
    JsonBatchCallback<Void> callback = new JsonBatchCallback<Void>() {
      public void onSuccess(Void nothing, HttpHeaders responseHeaders) {}
      public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
        String message = "Error deleting log file " + bucket + "/" + object + "\n" +
        "Error Message: " + e.getMessage() + "\n" +
        "Submitting a separate file delete request." + "\n" +
        "Processing of new logs will be halted until the deletion is cleared. ";
        log.severe(message);

        queue.add(withUrl("/deleteFailedFile").countdownMillis(5_000L)
        .param("bucket", bucket).param("object", object));
      }
    };

    return callback;
  }

  int getStatus() { return status; }
}
