package com.risevision.gcslogs.load;

import java.util.*;
import java.io.*;
import java.net.URISyntaxException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.appengine.tools.development.testing.LocalTaskQueueTestConfig;
import com.google.appengine.api.taskqueue.dev.LocalTaskQueue;
import com.google.appengine.api.taskqueue.dev.QueueStateInfo;
import com.google.appengine.api.taskqueue.*;
import com.risevision.gcslogs.BuildConfig;

public class LogLoaderTest {
  LocalServiceTestHelper helper;
  LogLoader logLoader;
  MockLoadJobInserter mockLoadJobInserter;

  int FILES_PER_BQ_LOAD_JOB =
  Integer.parseInt(BuildConfig.FILES_PER_BQ_LOAD_JOB.toString());

  @Before public void setUp() throws URISyntaxException, IOException {
    String queueFilePath =
    new File(this.getClass().getResource("/queue.xml").toURI()).getAbsolutePath();

    helper = new LocalServiceTestHelper
    (new LocalTaskQueueTestConfig().setQueueXmlPath(queueFilePath));
    helper.setUp();

    mockLoadJobInserter = new MockLoadJobInserter();

    logLoader = new LogLoader
    (new GCSLogsFetcher(new MockStorageObjectListRequestor()),
    mockLoadJobInserter);
  }

  @After public void tearDown() {
    helper.tearDown();
  }

  @Ignore @Test public void itDoesNotBeginIfDeleteJobsArePending() {
    //Ignored because local task count appears to be non-deterministic.
    //queue.fetchStatistics().getNumTasks() returns anywhere from 0 to 2000
    //on consecutive calls.
    QueueFactory.getQueue("storageLogDeletions")
    .add(TaskOptions.Builder.withTaskName("tasktest"));

    logLoader.begin();
    assertThat("it returned early", logLoader.logsFetcher, is(nullValue()));
  }

  @Test public void itLoadsFiles() {
    logLoader.loadLogs(Arrays.asList(new String[]{"1", "2" ,"3"}), "Usage");
    assertThat("it loads a few files", logLoader.loadCount, is(3));
  }

  @Test public void itHandlesEmptyParameter() {
    logLoader.loadLogs(Arrays.asList(new String[0]), "Usage");
    assertThat("it handles 0 files", logLoader.loadCount, is(0));
  }

  @Test public void itHandlesMaxLoadCount() {
    logLoader.loadLogs(Arrays.asList(new String[FILES_PER_BQ_LOAD_JOB]), "Usage");

    assertThat("it handles max load amount",
    logLoader.loadCount,
    is(FILES_PER_BQ_LOAD_JOB));
  }

  @Test public void itHandlesMaxLoadCountPlusOne() {
    logLoader.loadLogs(Arrays.asList(new String[FILES_PER_BQ_LOAD_JOB + 1]), "Usage");

    assertThat("it handles max load amount + 1",
    logLoader.loadCount,
    is(FILES_PER_BQ_LOAD_JOB + 1));
  }

  @Test public void itHandlesManyMultiplesOfMaxLoadCount() {
    logLoader.loadLogs(Arrays.asList(new String[FILES_PER_BQ_LOAD_JOB * 5 + 1]), "Usage");

    assertThat("it has the expected count",
    logLoader.loadCount,
    is(FILES_PER_BQ_LOAD_JOB * 5 + 1));
  }
}

