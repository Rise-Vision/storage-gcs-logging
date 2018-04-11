package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.IntegrationTestConfig;
import com.risevision.gcslogs.load.BQLoadJobInserterIT;
import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.api.services.bigquery.model.*;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.api.taskqueue.*;

public class DeleteLoadJobFilesServletHandlerIT {
  static final String bucket = IntegrationTestConfig.getBucket();

  LocalServiceTestHelper helper;
  LocalTaskQueue ltq;
  @Before public void setUp() throws URISyntaxException {
    String queueFilePath =
    new File(this.getClass().getResource("/queue.xml").toURI()).getAbsolutePath();

    helper = new LocalServiceTestHelper
    (new LocalTaskQueueTestConfig().setQueueXmlPath(queueFilePath));
    helper.setUp();

    ltq = LocalTaskQueueTestConfig.getLocalTaskQueue();
  }

  @After public void tearDown() {
    helper.tearDown();
  }

  @Test public void itGetsUris() throws Exception {
    BQLoadJobInserterIT inserter = new BQLoadJobInserterIT();
    inserter.loadJob();

    Map<String, String[]> params = new HashMap<>();
    params.put("jobId", new String[]{inserter.jobId});

    DeleteLoadJobFilesServletHandler deleter =
    new DeleteLoadJobFilesServletHandler(params, new GoogleCredentialProvider());

    assertThat("it has no uris", deleter.uris, is(nullValue()));

    int loopCount = 0;
    while (deleter.uris == null && loopCount < 9) {
      System.out.println
      ("[DEBUG] - fetching uris from job " + inserter.jobId);

      Thread.sleep(5000);
      deleter.getUrisFromBigqueryJob();
      loopCount += 1;
    }
    assertThat("it fetched uris", deleter.uris, is(not(nullValue())));
  }

  @Test public void itDeletesFilesFromJobId() throws Exception {
    Map<String, String[]> params = new HashMap<>();

    String uri = "gs://" + bucket + "/" +
    IntegrationTestConfig.get("URI_LIST_LOG_FILE");

    params.put("jobId", new String[]{"testJobId"});
    DeleteLoadJobFilesServletHandler deleter =
    new DeleteLoadJobFilesServletHandler(params, new GoogleCredentialProvider());

    deleter.uris = Arrays.asList(new String[]{uri});
    deleter.deleteFilesFromCloudStorage();
    assertThat("it deleted files", deleter.getStatus(), is(200));
  }
}
