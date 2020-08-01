package com.randominc.client.engine.core;

import com.randominc.client.engine.graphic.render.Scene;
import com.randominc.shared.hecs.EntityManager;

public interface GameMaster {

  Scene getCurrentScene();

  EntityManager getEntityManager();

  void update(float delta);

  void initialize(GameController gameController);
}
