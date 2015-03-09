package com.risevision.gcslogs.load;

import java.util.List;
import com.google.api.services.bigquery.model.Job;

interface LoadJobInserter {
  Job insertJob(List<String> fileUris, String logType);
}
