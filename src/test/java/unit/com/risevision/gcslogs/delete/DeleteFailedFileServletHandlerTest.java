package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.MockCredentialProvider;

import java.util.logging.Logger;
import java.util.Map;
import java.util.HashMap;
import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DeleteFailedFileServletHandlerTest {
  Map<String, String[]> params;
  DeleteFailedFileServletHandler deleter;

  @Before public void setUp() {
    params = new HashMap<String, String[]>();
    params.put("bucket", new String[]{"testBucket"});
    params.put("object", new String[]{"testObject"});
    deleter =
    new DeleteFailedFileServletHandler(params, new MockCredentialProvider());
  }

  @Test public void itExists() {
    assertThat("it exists", deleter, isA(DeleteFailedFileServletHandler.class));
  }

  @Test public void itGetsTheBucketAndObject() {
    assertThat("the bucket exists", deleter.bucket, is(not(nullValue())));
    assertThat("the object exists", deleter.object, is(not(nullValue())));
  }

  @Test(expected=RuntimeException.class) public void itThrowsWhenNoBucket() {
    params.remove("bucket");
    deleter =
    new DeleteFailedFileServletHandler(params, new MockCredentialProvider());
    assertThat("the bucket is null", deleter.bucket, is(nullValue()));
    assertThat("the object is null", deleter.object, is(nullValue()));
  }

  @Test(expected=RuntimeException.class) public void itThrowsWhenNoObject() {
    params.remove("object");
    deleter =
    new DeleteFailedFileServletHandler(params, new MockCredentialProvider());
    assertThat("the bucket is null", deleter.bucket, is(nullValue()));
    assertThat("the object is null", deleter.object, is(nullValue()));
  }
}


