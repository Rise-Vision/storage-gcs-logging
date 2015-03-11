package com.risevision.gcslogs.load;

import com.risevision.gcslogs.auth.CredentialProvider;

import java.io.*;
import java.util.logging.Logger;
import com.google.api.client.googleapis.util.Utils;
import com.google.api.services.storage.*;
import com.google.api.services.storage.model.*;

public class GCSStorageObjectListRequestor implements StorageObjectListRequestor {
  private static final Logger log =
  Logger.getLogger("gcslogs.GCSStorageObjectListRequestor");

  private Storage.Objects.List req;

  GCSStorageObjectListRequestor
  (String appName, String bucketName, CredentialProvider credentialProvider)
  throws IOException {
    Storage storage = new Storage.Builder(Utils.getDefaultTransport(),
    Utils.getDefaultJsonFactory(), credentialProvider.getCredential())
    .setApplicationName(appName).build();

    req = storage.objects().list(bucketName);
  }

  public Objects executeRequest() {
    Objects resp = null;
    try {
      resp = req.execute();
    } catch (IOException e) {
      log.warning("Error: " + e.getMessage() + "\n" + 
      "Could not execute list request.  Will reattempt on next log load.");
      e.printStackTrace();
    }

    return resp;
  }

  public void setPageToken(String token) {
    req.setPageToken(token);
  }
}
