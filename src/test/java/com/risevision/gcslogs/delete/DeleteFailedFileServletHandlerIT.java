package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.IntegrationTestConfig;
import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.util.*;
import java.io.IOException;
import java.io.File;
import java.net.URISyntaxException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class DeleteFailedFileServletHandlerIT {
  static final String bucket = IntegrationTestConfig.getBucket();
  static final String object = IntegrationTestConfig.get("DELETE_FAILED_FILE");

  @Test public void itDeletesAFile() throws Exception {
    Map<String, String[]> params = new HashMap<>();

    params.put("bucket", new String[]{bucket});
    params.put("object", new String[]{object});
    DeleteFailedFileServletHandler deleter =
    new DeleteFailedFileServletHandler(params, new GoogleCredentialProvider());

    deleter.deleteFile();
    assertThat("it deleted the file", deleter.getStatus(), is(200));
  }
}
