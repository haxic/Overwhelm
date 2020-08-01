package com.randominc.shared.debug;

import java.io.PrintStream;

public class DefaultDebugLogProvider implements DebugLogProvider {
  private final PrintStream printStream;

  public DefaultDebugLogProvider() {
    printStream = System.err;
  }

  public void error(String className, String message) {
    System.err.println(String.format("%s - %s", className, message));
    try {
      throw new Exception();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void error(String className, String message, Exception exception) {
    System.err.println(String.format("%s - %s", className, message));
    exception.printStackTrace();
  }

  public void debug(String className, String message) {
    className = (className + "                                        ").substring(0, 35);
    System.out.println(String.format("%s%s", className, message));
  }

  public PrintStream getPrintStream() {
    return printStream;
  }

  @Override
  public DebugLog getDebugLog(Object object) {
    return new DefaultDebugLog(this, object.getClass().getSimpleName());
  }
}
