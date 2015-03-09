package com.risevision.gcslogs.load;

import java.io.IOException;
import com.google.api.services.storage.model.*;

interface StorageObjectListRequestor {
  Objects executeRequest();
  void setPageToken(String token);
}
