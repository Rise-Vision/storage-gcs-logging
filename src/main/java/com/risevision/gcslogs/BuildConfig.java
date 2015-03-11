package com.risevision.gcslogs;

import java.io.*;
import java.util.Properties;
import java.util.logging.Logger;
import static com.risevision.gcslogs.alert.AlertService.alert;

public enum BuildConfig {
  LOGS_BUCKET_NAME("LOGS_BUCKET_NAME"),
  DATASET_ID("DATASET_ID"),
  PROJECT_ID("PROJECT_ID"),
  STORAGE_APP_NAME("STORAGE_APP_NAME"),
  RVMEDIA_ID("RVMEDIA_ID"),
  RVMEDIA_P12_PATH("RVMEDIA_P12_PATH"),
  USAGE_LOG_SCHEMA_PATH("USAGE_LOG_SCHEMA_PATH"),
  STORAGE_LOG_SCHEMA_PATH("STORAGE_LOG_SCHEMA_PATH"),
  STORAGE_SCOPE("STORAGE_SCOPE"),
  EMAIL_SCOPE("EMAIL_SCOPE"),
  BQ_SCOPE("BQ_SCOPE"),
  FILES_PER_BQ_LOAD_JOB("FILES_PER_BQ_LOAD_JOB");

  private static InputStream file;
  private final String key;
  private static Properties props = new Properties();
  private static final String FILE_PATH = "/build.properties";
  private static final Logger log = Logger.getLogger("gcslogs.BuildConfig");
  static {
    try{
      InputStream file = BuildConfig.class.getResourceAsStream(FILE_PATH);
      props.load(file);
    } catch (IOException e) {
      alert("Could not load config variables from " + FILE_PATH, e.getMessage());
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  BuildConfig(String key) {
    this.key = key;
  }

  public String toString() {
    if (!props.containsKey(key)) {
      alert("Build configuration key [" + key + "] does not exist.");
      throw new RuntimeException("Invalid build configuration key");
    }
    return props.getProperty(key);
  }
}
