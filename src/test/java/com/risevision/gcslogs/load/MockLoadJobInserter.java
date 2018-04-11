package com.risevision.gcslogs.load;

import java.util.List;
import com.google.api.services.bigquery.model.*;

class MockLoadJobInserter implements LoadJobInserter {
  public Job insertJob(List<String> fileUris, String logType) {
    return new Job().setJobReference(new JobReference().setJobId("jobId-12345"));
  }
}
