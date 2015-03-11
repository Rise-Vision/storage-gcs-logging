package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.CredentialProvider;

import java.util.logging.Logger;
import java.util.Map;

import java.io.IOException;
import com.google.api.services.storage.*;
import com.google.api.client.googleapis.util.Utils;

import static com.risevision.gcslogs.BuildConfig.*;

class DeleteFailedFileServletHandler {
  static final int STATUS_OK = 200;
  static final int STATUS_PROCESSING = 102;
  private static final Logger log =
  Logger.getLogger("gcslogs.DeleteFailedFileHandler");

  private int status = STATUS_PROCESSING;
  private Storage storage;

  String bucket;
  String object;

  DeleteFailedFileServletHandler(
  Map<String, String[]> params, CredentialProvider credentialProvider) {
    if (!params.containsKey("bucket") || !params.containsKey("object")) {
      throw new RuntimeException("No file to delete");
    }

    bucket = params.get("bucket")[0];
    object = params.get("object")[0];

    storage = new Storage.Builder(Utils.getDefaultTransport(),
    Utils.getDefaultJsonFactory(), credentialProvider.getCredential())
    .setApplicationName(STORAGE_APP_NAME.toString()).build();
  }

  void deleteFile() {
    try {
      storage.objects().delete(bucket, object).execute();
    } catch (IOException e) {
      String message = "Error deleting log file " + bucket + "/" + object + "\n" +
      "Error Message: " + e.getMessage() + "\n" +
      "Task will be retried in time according to the task queue settings.\n" +
      "Processing of new logs will be halted until the deletion is successful. ";
      log.warning(message);
      e.printStackTrace();
      this.status = STATUS_PROCESSING;
      return;
    }

    this.status = STATUS_OK;
  }

  int getStatus() { return status; }
}

