package com.risevision.gcslogs;

import java.util.Properties;
import java.io.IOException;

public class IntegrationTestConfig {
  private static final String propertiesPath = "/integration-test-files.properties";
  static final Properties props;

  static {
    props = new Properties();
    try {
      props.load
      (IntegrationTestConfig.class.getResourceAsStream(propertiesPath));
    } catch (IOException e) {
      System.out.println("[ERROR] - Could not load integration test property file");
    }
  }

  public static String get(String key) {
     return props.getProperty(key);
  }

  public static String getBucket() {
    return props.getProperty("INTEGRATION_TEST_BUCKET");
  }
}
