package com.risevision.gcslogs.alert;

import java.util.logging.Logger;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import com.google.appengine.tools.development.testing.*;

public class AlertServiceIT {
  private static final Logger log =
  Logger.getLogger("gcslogs.AlertServiceIT");

  private final LocalServiceTestHelper helper =
  new LocalServiceTestHelper(new LocalURLFetchServiceTestConfig());

  @Before
  public void setUp() {
    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test public void itAlerts() {
    assertThat("it alerted", AlertService.alert("Test message", null), is(200));
  }
}

