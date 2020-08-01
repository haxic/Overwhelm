package com.randominc.client.engine.core;

public class GameLoopFactory {

  public GameLoop createDefaultGameLoop(GameEngineController gameEngineController) {
    DefaultGameLoop defaultGameLoop = new DefaultGameLoop(gameEngineController);
    return defaultGameLoop;
  }
}
