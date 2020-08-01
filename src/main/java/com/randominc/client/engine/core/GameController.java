package com.randominc.client.engine.core;

public interface GameController {
  void toggleFullscreen();

  void exitGame();

  void toggleCursor();

  void showCursor();

  void hideCursor();

  boolean isCursorShown();

  void centerCursor();
}
