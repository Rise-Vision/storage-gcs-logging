package com.risevision.gcslogs.delete;

import com.risevision.gcslogs.auth.GoogleCredentialProvider;

import java.io.IOException;
import javax.servlet.http.*;
import javax.servlet.ServletException;
import java.util.logging.Logger;

public class DeleteLoadJobFilesServlet extends HttpServlet {
  private static final Logger log = Logger.getLogger
  ("gcslogs.DeleteLoadJobFilesServlet");

  public void doPost (HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException  {
    log.info("DeleteLoadJobFilesServlet: deleting files");

    DeleteLoadJobFilesServletHandler deleter = new DeleteLoadJobFilesServletHandler
    (request.getParameterMap(), new GoogleCredentialProvider());

    log.info("Retry count: " + request.getHeader("X-AppEngine-TaskRetryCount"));
    int retries = Integer.parseInt(request.getHeader("X-AppEngine-TaskRetryCount"));

    if (retries > 0 && retries % 7 == 0) {
      log.severe("A log deletion task is blocking log processing.  " +
      "There may be a log load job that can't complete without error, " +
      "or the deletion job itself can't be submitted without error.  " +
      "No further load jobs will be initiated until the delete task is cleared.");
    }

    deleter.deleteFiles();
    response.setStatus(deleter.getStatus());
  }
}
