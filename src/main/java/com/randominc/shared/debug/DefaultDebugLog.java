package com.randominc.shared.debug;

import java.io.PrintStream;

public class DefaultDebugLog implements DebugLog {

  private final DefaultDebugLogProvider defaultDebugLogProvider;
  private final String className;
  private boolean enabled = false;

  public DefaultDebugLog(DefaultDebugLogProvider defaultDebugLogProvider, String className) {
    this.defaultDebugLogProvider = defaultDebugLogProvider;
    this.className = className;
  }

  @Override
  public void error(String message) {
    defaultDebugLogProvider.error(className, message);
  }

  @Override
  public void error(String message, Exception exception) {
    defaultDebugLogProvider.error(className, message, exception);
  }

  @Override
  public void debug(String message) {
    if (enabled) {
      defaultDebugLogProvider.debug(className, message);
    }
  }

  @Override
  public void info(String message) {
    defaultDebugLogProvider.debug(className, message);
  }

  @Override
  public PrintStream getPrintStream() {
    return defaultDebugLogProvider.getPrintStream();
  }
}
