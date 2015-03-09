package com.risevision.gcslogs.alert;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.api.client.googleapis.util.Utils;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequestFactory;

import static com.risevision.gcslogs.BuildConfig.*;

public class AlertService {
  private static final Logger log =
  Logger.getLogger("gcslogs.AlertService");

  private static HttpRequestFactory requestor =
  Utils.getDefaultTransport().createRequestFactory();

  private static String URL = "https://http-api-dot-logger-dot-rvacore-test.appspot.com/submit?token=gcs-logs&environment=testenvironment&severity=alert&error_message=MESSAGE&error_details=DETAILS&logger_version=1";

  public static void alert(String msg, String details) {
    if (msg == null) {return;}
    if (details == null) {details = "";}

    String url = URL.replace("MESSAGE", msg).replace("DETAILS", details);
    log.info("TESTING");
    try {
      requestor.buildPostRequest(new GenericUrl(url), null).execute();
    } catch (IOException e) {
      log.severe("Error sending alert \"message " + msg + "\".\n" + e.getMessage());
      e.printStackTrace();
    }
  }
}
