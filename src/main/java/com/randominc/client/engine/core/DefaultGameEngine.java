package com.randominc.client.engine.core;

import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.render.renderer.RenderManager;
import com.randominc.client.engine.window.Window;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.Objects;

public class DefaultGameEngine implements GameController, GameEngineController {

  private final DebugLog debugLog;
  private final Window window;
  private final MeshFactory meshFactory;
  private final RenderManager renderManager;
  private final GameMaster gameMaster;
  private final GameLoopFactory gameLoopFactory;
  private final GameLoop gameLoop;
  private final WindowInputProvider windowInputProvider;
  private boolean isExitingGame;

  public DefaultGameEngine(
      Window window,
      MeshFactory meshFactory,
      RenderManager renderManager,
      GameMaster gameMaster,
      WindowInputProvider windowInputProvider,
      GameLoopFactory gameLoopFactory) {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.window = Objects.requireNonNull(window);
    this.meshFactory = Objects.requireNonNull(meshFactory);
    this.renderManager = Objects.requireNonNull(renderManager);
    this.gameMaster = Objects.requireNonNull(gameMaster);
    this.windowInputProvider = Objects.requireNonNull(windowInputProvider);
    this.gameLoopFactory = Objects.requireNonNull(gameLoopFactory);
    debugLog.info("Initializing game engine. Don't forget to start it!");
    gameLoop = gameLoopFactory.createDefaultGameLoop(this);
    gameMaster.initialize(this);
  }

  public void start() {
    debugLog.info("Starting game engine.");
    window.show();
    gameLoop.run();
  }

  public boolean isRunning() {
    return !window.isClosing() && !isExitingGame;
  }

  public void update(float delta) {
    gameMaster.update(delta);
    window.update();
  }

  public void render() {
    if (window.isReadyToRender()) {
      renderManager.render(gameMaster.getCurrentScene());
      window.render();
    }
  }

  public void cleanUp() {
    debugLog.info("Game closing, cleaning up.");
    window.cleanUp();
    meshFactory.cleanUp();
    debugLog.info("Clean up done.");
  }

  @Override
  public void toggleFullscreen() {
    window.toggleFullscreen();
  }

  @Override
  public void exitGame() {
    isExitingGame = true;
  }

  @Override
  public void toggleCursor() {
    window.toggleCursor();
  }

  @Override
  public void showCursor() {
    window.showCursor();
  }

  @Override
  public void hideCursor() {
    window.hideCursor();
  }

  @Override
  public boolean isCursorShown() {
    return window.isCursorShown();
  }

  @Override
  public void centerCursor() {
    window.centerCursor();
  }
}
