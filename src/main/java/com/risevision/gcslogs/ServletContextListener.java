package com.risevision.gcslogs;

import java.util.logging.Logger;
import javax.servlet.ServletContextEvent;
import static com.risevision.gcslogs.BuildConfig.*;

public class ServletContextListener implements javax.servlet.ServletContextListener {
  private static final Logger log = Logger.getAnonymousLogger();

  @Override
  public void contextInitialized(ServletContextEvent event) {
    log.info("Initializing servlet.");
    log.info("Using storage project: " + STORAGE_APP_NAME);
    log.info("Storage project id: " + PROJECT_ID);
    log.info("Credential User:" + RVMEDIA_ID);
  }

  @Override
  public void contextDestroyed(ServletContextEvent event) {
  }
}
