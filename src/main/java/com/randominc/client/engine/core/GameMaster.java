package com.randominc.client.engine.core;

import com.randominc.client.engine.graphic.render.Scene;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.shared.hecs.EntityManager;

public interface GameMaster {

  void initialize(GameEngine gameEngine, WindowInputProvider windowInputProvider);

  Scene getCurrentScene();

  EntityManager getEntityManager();

  void update(float delta);
}
