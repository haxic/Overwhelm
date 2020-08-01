package com.randominc.client.engine.core;

import org.joml.Vector2f;

public interface GameEngine {
  void toggleFullscreen();

  void exitGame();

  void toggleCursor();

  void showCursor();

  void hideCursor();

  boolean isCursorShown();

  void centerCursor();

  Vector2f getCenter(Vector2f center);
}
