package com.randominc.launcher;

import com.randominc.client.engine.core.GameEngineController;
import com.randominc.client.engine.core.GameLoop;
import com.randominc.client.engine.core.GameMaster;
import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.render.renderer.RenderManager;
import com.randominc.client.engine.window.Window;
import com.randominc.shared.hecs.EntityManager;

public class TestLauncher {

  public static void main(String[] args) {
    Window window = new Window("Test", 1000, 1000);
    MeshFactory meshFactory = new MeshFactory();
    RenderManager renderManager = new RenderManager();

    EntityManager entityManager = new EntityManager();
    GameMaster gameMaster = new TestGameMaster(entityManager, meshFactory);
    GameEngineController gameEngineController =
        new GameEngineController(window, meshFactory, renderManager, gameMaster);
    GameLoop gameLoop = new GameLoop(gameEngineController);

    gameLoop.initialize();
    gameLoop.run();
  }
}
