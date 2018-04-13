package com.risevision.gcslogs.load;

import com.risevision.gcslogs.IntegrationTestConfig;
import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.util.*;
import java.io.IOException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.api.services.bigquery.model.*;

public class BQLoadJobInserterIT {
  static final String bucket = IntegrationTestConfig.getBucket();
  public static String jobId;

  @Ignore //Will be run by DeleteLoadJobFilesServletHandlerIT
  @Test public void loadJob() throws IOException {
    BQLoadJobInserter inserter =
    new BQLoadJobInserter(new GoogleCredentialProvider());

    String object = IntegrationTestConfig.get("JOB_LOAD_FILE");
    String uri = "gs://" + bucket + "/" + object;
    Job job = inserter.insertJob(Arrays.asList(new String[]{uri}), "Test");

    jobId = job.getJobReference().getJobId();

    assertThat("it submitted", jobId, is(not(nullValue())));

    assertThat("it submitted without error", job.getStatus().getErrorResult(),
    is(nullValue()));
  }
}
