package com.risevision.gcslogs.load;

import java.util.List;
import java.util.ArrayList;
import java.io.IOException;
import java.util.logging.Logger;
import com.google.api.services.storage.*;
import com.google.api.services.storage.model.*;

import static com.risevision.gcslogs.BuildConfig.*;

public class GCSLogsFetcher {
  private List<String> usageLogs = new ArrayList<>();
  private List<String> storageLogs = new ArrayList<>();
  private StorageObjectListRequestor req;
  private static final String LOGS_URI_PATH =
  "gs://" + LOGS_BUCKET_NAME + "/";

  private static final Logger log = Logger.getLogger("gcslogs.GCSLogsFetcher");

  GCSLogsFetcher(StorageObjectListRequestor requestor) {
    this.req = requestor;
  }

  void fetchLogs() {
    Objects resp;

    do {
      resp = (Objects) req.executeRequest();
      if (resp.getItems() == null) { return; }

      for (StorageObject object : resp.getItems()) {
        String objectName = object.getName();
        if (objectName.indexOf("_usage_") > -1) {
          usageLogs.add(LOGS_URI_PATH + objectName);
        } else {
          storageLogs.add(LOGS_URI_PATH + objectName);
        }
      }
      req.setPageToken(resp.getNextPageToken());
    } while (resp.getNextPageToken() != null && (usageLogs.size() + storageLogs.size()) < 50000);
  }

  List<String> getUsageLogs() {
    return usageLogs;
  }

  List<String> getStorageLogs() {
    return storageLogs;
  }
}
