package com.randominc.shared.debug;

import java.io.PrintStream;

public interface DebugLog {
  void error(String message);

  void error(String message, Exception exception);

  void debug(String message);

  PrintStream getPrintStream();
}
