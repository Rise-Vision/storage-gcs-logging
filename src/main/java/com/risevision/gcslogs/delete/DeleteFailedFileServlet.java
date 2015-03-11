package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.util.logging.Logger;

public class DeleteFailedFileServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger
  ("gcslogs.DeleteFailedFileServlet");

  private static final String errorMessage =
  "A log file deletion task is blocking log processing.\n" +
  "No further load jobs will be initiated until the delete task is cleared.";

  public void doPost (HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException  {
    log.info("DeleteFailedFileServlet: deleting file");

    DeleteFailedFileServletHandler deleter = new DeleteFailedFileServletHandler
    (request.getParameterMap(), new GoogleCredentialProvider());

    log.info("Retry count: " + request.getHeader("X-AppEngine-TaskRetryCount"));
    int retries = Integer.parseInt(request.getHeader("X-AppEngine-TaskRetryCount"));

    deleter.deleteFile();
    response.setStatus(deleter.getStatus());

    if (retries > 0 && retries % 8 == 0 && deleter.getStatus() != 200) {
      com.risevision.gcslogs.alert.AlertService.alert
      ("Problems deleting a log file", errorMessage);
    }
  }
}

