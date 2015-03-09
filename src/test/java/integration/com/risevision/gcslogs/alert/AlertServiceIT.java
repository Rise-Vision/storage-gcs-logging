package com.risevision.gcslogs.alert;

import java.util.logging.Logger;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class AlertServiceIT {
  private static final Logger log =
  Logger.getLogger("gcslogs.AlertServiceIT");

  @Test public void itAlerts() {
    assertThat("it alerted", AlertService.alert("Test message", null), is(200));
  }
}

