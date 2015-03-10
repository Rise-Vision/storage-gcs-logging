package com.risevision.gcslogs.alert;

import java.util.logging.Logger;
import java.io.File;

import org.junit.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import com.google.appengine.tools.development.testing.*;

public class AlertServiceIT {
  private static final Logger log =
  Logger.getLogger("gcslogs.AlertServiceIT");

  private String queuePath;

  private LocalServiceTestHelper helper;

  @Before
  public void setUp() {
    try {
    queuePath = new File(this.getClass().getResource("/queue.xml").toURI())
    .getAbsolutePath();
    } catch (Exception e) {
      e.printStackTrace();
    }

    helper = new LocalServiceTestHelper
    (new LocalTaskQueueTestConfig().setQueueXmlPath(queuePath),
    new LocalModulesServiceTestConfig().addDefaultModuleVersion()
    .addAutomaticScalingModuleVersion("logger","1"));

    helper.setUp();
  }

  @After
  public void tearDown() {
    helper.tearDown();
  }

  @Test public void itAlerts() {
    assertThat("it submitted a task",
    AlertService.alert("Test message", null).getQueueName(),
    is("logger"));
  }
}

