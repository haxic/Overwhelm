package com.randominc.client.engine.graphic.utility;

import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class FileUtil {

  private static FileUtil instance;
  private final DebugLog debugLog;

  private FileUtil() {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
  }

  public static FileUtil getInstance() {
    if (instance == null) {
      instance = new FileUtil();
    }
    return instance;
  }

  public String loadAsString(String file) {
    StringBuilder result = new StringBuilder();
    try {
      BufferedReader reader = new BufferedReader(new FileReader(file));
      String buffer = "";
      while ((buffer = reader.readLine()) != null) {
        result.append(buffer + '\n');
      }
      reader.close();
    } catch (IOException e) {
      debugLog.error("Failed to load file.", e);
    }
    return result.toString();
  }
}
