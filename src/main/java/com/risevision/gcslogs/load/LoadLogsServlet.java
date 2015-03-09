package com.risevision.gcslogs.load;

import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.util.logging.Logger;
import static com.risevision.gcslogs.BuildConfig.*;

public class LoadLogsServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger("gcslogs.LoadLogsServlet");
  private static final String appName = STORAGE_APP_NAME.toString();
  private static final String bucketName = LOGS_BUCKET_NAME.toString();

  public void doGet (HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException  {
    log.info("Loading logs from: \n" + appName + "\n" + bucketName);
    GoogleCredentialProvider credentialProvider = new GoogleCredentialProvider();
    (new LogLoader
    (new GCSLogsFetcher
    (new GCSStorageObjectListRequestor
    (appName, bucketName, credentialProvider)),
    new BQLoadJobInserter(credentialProvider)))
    .begin();
  }
}
