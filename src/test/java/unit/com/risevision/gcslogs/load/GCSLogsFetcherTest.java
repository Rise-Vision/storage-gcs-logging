package com.risevision.gcslogs.load;

import java.util.List;
import java.util.Arrays;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import com.google.api.services.storage.model.*;

public class GCSLogsFetcherTest {
  @Test public void itExists() {
    assertThat("it exists", new GCSLogsFetcher(null), isA(GCSLogsFetcher.class));
  }

  @Test public void itFetchesUsageLogs() {
    StorageObject object = new StorageObject();
    object.setName("_usage_test");

    Objects mockResponse = new Objects();

    mockResponse.setItems(Arrays.asList(object));

    MockStorageObjectListRequestor req = new MockStorageObjectListRequestor();
    req.setObjectsPages(new Objects[]{mockResponse});

    GCSLogsFetcher fetcher = new GCSLogsFetcher(req);
    fetcher.fetchLogs();

    List<String> usageLogs = fetcher.getUsageLogs();
    assertThat("it has usage logs", usageLogs.size(), equalTo(1));
  }

  @Test public void itFetchesMultiplePages() {
    Objects mockResponsePage1 = new Objects();
    mockResponsePage1.setNextPageToken("ABC");

    StorageObject object = new StorageObject();
    object.setName("_usage_test_name");
    mockResponsePage1.setItems(Arrays.asList(object));

    Objects mockResponsePage2 = new Objects();
    mockResponsePage2.setItems(Arrays.asList(object));
    mockResponsePage2.setNextPageToken(null);

    MockStorageObjectListRequestor req = new MockStorageObjectListRequestor();
    req.setObjectsPages(new Objects[]{mockResponsePage1, mockResponsePage2});

    GCSLogsFetcher fetcher = new GCSLogsFetcher(req);
    fetcher.fetchLogs();

    List<String> usageLogs = fetcher.getUsageLogs();
    assertThat("it has multiple usage logs", usageLogs.size(), equalTo(2));
  }

  @Test public void itFetchesStorageLogs() {
    StorageObject object = new StorageObject();
    object.setName("_storage_test");

    Objects mockResponse = new Objects();

    mockResponse.setItems(Arrays.asList(object));

    MockStorageObjectListRequestor req = new MockStorageObjectListRequestor();
    req.setObjectsPages(new Objects[]{mockResponse});

    GCSLogsFetcher fetcher = new GCSLogsFetcher(req);
    fetcher.fetchLogs();

    List<String> storageLogs = fetcher.getStorageLogs();
    assertThat("it has storage logs", storageLogs.size(), equalTo(1));
  }
}
