package com.randominc.client.engine.core;

public interface GameEngineController {

  boolean isRunning();

  void update(float delta);

  void render();

  void cleanUp();
}
