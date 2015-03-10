package com.risevision.gcslogs.alert;

import java.io.IOException;
import java.util.logging.Logger;

import com.google.api.client.googleapis.util.Utils;
import com.google.appengine.api.modules.ModulesServiceFactory;
import com.google.api.client.http.EmptyContent; 
import com.google.appengine.api.urlfetch.*;
import java.net.URL;
import static com.risevision.gcslogs.BuildConfig.*;
import com.google.api.client.util.escape.PercentEscaper;

public class AlertService {
  private static final Logger log =
  Logger.getLogger("gcslogs.AlertService");

  private static URLFetchService fetcher =
  URLFetchServiceFactory.getURLFetchService();

  private static PercentEscaper escaper = new PercentEscaper("", false);
  private static FetchOptions opts = FetchOptions.Builder.doNotFollowRedirects();

  private static String alertHost =
  "http-api-dot-logger-dot-rvacore-test.appspot.com";

  private static String URL = "http://" + alertHost + "/submit?token=gcs-logs&environment=testenvironment&severity=alert&error_message=MESSAGE&error_details=DETAILS&logger_version=1";

  public static int alert(String msg, String details) {
    if (msg == null) {return 503;}
    if (details == null) {details = "";}
    HTTPResponse resp = null;

    String url = URL.replace
    ("MESSAGE", escaper.escape(msg)).replace("DETAILS", escaper.escape(details));

    log.info("TESTING " + url);
    try {
      resp = fetcher.fetch(new HTTPRequest(new URL(url), HTTPMethod.POST, opts));
    } catch (IOException e) {
      log.severe("Error sending alert \"message " + msg + "\".\n" + e.getMessage());
      e.printStackTrace();
    }
    log.info("TESTING " + new String(resp.getContent()));
    for (HTTPHeader header : resp.getHeaders()) {
      log.info(header.getName() + ":" + header.getValue());
    }
    log.info("TESTING " + resp.getFinalUrl());
    return resp == null ? 503 : resp.getResponseCode();
  }
}
