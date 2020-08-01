package com.randominc.client.engine.core;

import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.Objects;

public class DefaultGameLoop implements GameLoop {
  // public class DefaultGameLoop implements Runnable {
  private final DebugLog debugLog;
  private final GameEngineController gameEngineController;
  private boolean started = false;
  //  private final Thread gameThread;

  public DefaultGameLoop(GameEngineController gameEngineController) {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.gameEngineController = Objects.requireNonNull(gameEngineController);
    //    gameThread = new Thread(this);
  }

  @Override
  //  public void run() {
  public void run() {
    if (started) {
      debugLog.error("The game loop has already running.");
      return;
    }
    started = true;
    //    gameThread.start();

    long lastTime = System.nanoTime();
    while (gameEngineController.isRunning()) {
      long newTime = System.nanoTime();
      float delta = (newTime - lastTime) / 1000000.0f;
      lastTime = newTime;
      debugLog.info("");
      gameEngineController.update(delta);
      gameEngineController.render();
    }
    gameEngineController.cleanUp();
  }
}
