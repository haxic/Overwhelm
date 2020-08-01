package com.randominc.client.engine.core;

import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.render.renderer.RenderManager;
import com.randominc.client.engine.window.Window;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.Objects;
import org.joml.Vector2f;

public class GameEngineController implements GameEngine {

  private final DebugLog debugLog;
  private final Window window;
  private final MeshFactory meshFactory;
  private final RenderManager renderManager;
  private final GameMaster gameMaster;
  private WindowInputProvider windowInputProvider;
  private boolean isExitingGame;

  public GameEngineController(
      Window window, MeshFactory meshFactory, RenderManager renderManager, GameMaster gameMaster) {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.window = Objects.requireNonNull(window);
    this.meshFactory = Objects.requireNonNull(meshFactory);
    this.renderManager = Objects.requireNonNull(renderManager);
    this.gameMaster = Objects.requireNonNull(gameMaster);
  }

  public void initialize() {
    window.create();
    windowInputProvider = window.getInputProvider();
    gameMaster.initialize(this, windowInputProvider);
    renderManager.initialize();
    window.show();
  }

  public boolean isRunning() {
    return !window.isClosing() && !isExitingGame;
  }

  public void update(float delta) {
    gameMaster.update(delta);
    window.update();
  }

  public void render() {
    renderManager.render(gameMaster.getCurrentScene());
    window.render();
  }

  public void cleanUp() {
    debugLog.debug("Game closing, cleaning up.");
    window.cleanUp();
    meshFactory.cleanUp();
    debugLog.debug("Clean up done.");
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

  @Override
  public Vector2f getCenter(Vector2f destination) {
    return window.getCenter(destination);
  }
}
