package com.randominc.launcher;

import com.randominc.client.data.DefaultGameModelFactory;
import com.randominc.client.data.DefaultMeshProvider;
import com.randominc.client.data.DefaultModelProvider;
import com.randominc.client.data.DefaultTextureProvider;
import com.randominc.client.engine.core.DefaultGameEngine;
import com.randominc.client.engine.core.GameLoopFactory;
import com.randominc.client.engine.core.GameMaster;
import com.randominc.client.engine.graphic.preprocessing.MeshFactory;
import com.randominc.client.engine.graphic.preprocessing.TextureFactory;
import com.randominc.client.engine.graphic.render.renderer.RenderManager;
import com.randominc.client.engine.window.Window;
import com.randominc.client.system.UITextSystem;
import com.randominc.shared.hecs.EntityManager;

public class TestLauncher {

  public static void main(String[] args) {
    Window window = new Window("Test", 1000, 1000);
    MeshFactory meshFactory = new MeshFactory();
    TextureFactory textureFactory = new TextureFactory();
    DefaultMeshProvider defaultMeshProvider = new DefaultMeshProvider();
    DefaultModelProvider defaultModelProvider = new DefaultModelProvider();
    DefaultTextureProvider defaultTextureProvider = new DefaultTextureProvider();
    DefaultGameModelFactory defaultGameModelFactory =
        new DefaultGameModelFactory(meshFactory, textureFactory, defaultMeshProvider, defaultTextureProvider, defaultModelProvider);
    RenderManager renderManager = new RenderManager(defaultMeshProvider);
    EntityManager entityManager = new EntityManager();

    UITextSystem uiTextManager = new UITextSystem(entityManager, meshFactory, defaultTextureProvider);
    GameMaster gameMaster =
        new TestGameMaster(
            entityManager,
            uiTextManager,
            defaultGameModelFactory,
            defaultMeshProvider,
            defaultModelProvider,
            window.getInputProvider());

    GameLoopFactory gameLoopFactory = new GameLoopFactory();

    DefaultGameEngine defaultGameEngine =
        new DefaultGameEngine(
            window,
            meshFactory,
            renderManager,
            gameMaster,
            window.getInputProvider(),
            gameLoopFactory);

    defaultGameEngine.start();
  }
}
