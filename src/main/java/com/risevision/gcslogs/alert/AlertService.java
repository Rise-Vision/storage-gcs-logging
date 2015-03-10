package com.risevision.gcslogs.alert;

import java.io.IOException;
import java.util.logging.Logger;

import static com.risevision.gcslogs.BuildConfig.*;

import com.google.appengine.api.taskqueue.TaskHandle;
import com.google.appengine.api.modules.ModulesServiceFactory;
import static com.google.appengine.api.taskqueue.TaskOptions.Builder.*;

public class AlertService {
  private static final com.google.appengine.api.taskqueue.Queue queue =
  com.google.appengine.api.taskqueue.QueueFactory.getQueue("logger");

  private static String targetModuleHost = 
  ModulesServiceFactory.getModulesService().getVersionHostname("logger", null);

  public static TaskHandle alert(String msg, String details) {
    if (msg == null) {return new TaskHandle(null, "");}
    if (details == null) {details = "";}

    return queue.add(withUrl("/queue")
    .param("task", "submit")
    .param("token", "gcs-logs")
    .param("environment", "testenviro")
    .param("severity", "alert")
    .param("error_message", msg)
    .param("error_details", details)
    .param("logger_version", "1")
    .header("host", targetModuleHost));
  }
}
