package com.risevision.gcslogs.load;

import com.risevision.gcslogs.IntegrationTestConfig;
import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.util.List;
import java.io.IOException;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.api.services.storage.model.*;

import static com.risevision.gcslogs.BuildConfig.*;

public class GCSStorageObjectListRequestorIT {
  static final String bucket = IntegrationTestConfig.getBucket();

  @Test public void itFetchesUsageLogs() throws IOException {
    GCSStorageObjectListRequestor requestor;

    requestor = new GCSStorageObjectListRequestor
    (STORAGE_APP_NAME.toString(), bucket, new GoogleCredentialProvider());

    Objects objects = requestor.executeRequest();
    assertThat("it has usage logs", objects.getItems(), is(not(nullValue())));
  }
}
