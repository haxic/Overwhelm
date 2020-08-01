package com.randominc.client.data;

import com.randominc.client.engine.graphic.model.Model;

public interface ModelProvider {

  Model getDefaultCube();

  Model getDefaultQuad();

  Model getTestTerrain();
}
