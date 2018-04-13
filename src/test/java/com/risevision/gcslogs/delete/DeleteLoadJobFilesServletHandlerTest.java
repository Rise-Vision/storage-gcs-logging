package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.MockCredentialProvider;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static com.risevision.gcslogs.BuildConfig.*;

public class DeleteLoadJobFilesServletHandlerTest {
  Map<String, String[]> params;
  DeleteLoadJobFilesServletHandler deleter;

  @Before public void setUp() {
    params = new HashMap<String, String[]>();
    params.put("jobId", new String[]{"testId"});
    deleter =
    new DeleteLoadJobFilesServletHandler(params, new MockCredentialProvider());
  }

  @Test public void itExists() {
    assertThat("it exists", deleter, isA(DeleteLoadJobFilesServletHandler.class));
  }

  @Test public void itGetsTheJobId() {
    assertThat("the id exists", deleter.jobId, is(not(nullValue())));
  }

  @Test public void itExtractsObjectNamesFromUris() {
    String uri = "gs://" + LOGS_BUCKET_NAME.toString() + "/folder/name";

    assertThat("the name is correct", deleter.extractObjectName(uri),
    is("folder/name"));
  }

  @Test public void itExtractsBucketNamesFromUris() {
    String uri = "gs://" + LOGS_BUCKET_NAME.toString() + "/folder/name";

    assertThat("the bucket is correct", deleter.extractBucketName(uri),
    is(LOGS_BUCKET_NAME.toString()));
  }
}

