package com.risevision.gcslogs.load;

import com.google.api.services.storage.model.*;

class MockStorageObjectListRequestor implements StorageObjectListRequestor {
  private String pageToken;
  private Objects[] objectsPages = new Objects[1];

  public Objects executeRequest() {
    return pageToken == null ? objectsPages[0] : objectsPages[1];
  }

  public void setPageToken(String token) {
    pageToken = token;
  }

  public void setObjectsPages(Objects[] objectsPages) {
    this.objectsPages = objectsPages;
  }
}

