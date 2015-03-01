package com.risevision.gcslogs;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.google.appengine.api.utils.SystemProperty;

public final class Globals {
  public static final Boolean devserver = 
  SystemProperty.environment.value() == 
  SystemProperty.Environment.Value.Development;

  private static final Properties globals;

  static {
    globals = new Properties();
    InputStream fileData = null;

    try {
      fileData = Globals.class.getResourceAsStream("/build.properties");
      globals.load(fileData);
    } catch (IOException e) {
      e.printStackTrace();
      throw new RuntimeException(e);
    }
  }

  public static String global(String key) {
    return globals.getProperty(key);
  }
}
