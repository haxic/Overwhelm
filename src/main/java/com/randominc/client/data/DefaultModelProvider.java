package com.randominc.client.data;

import com.randominc.client.engine.graphic.model.Model;

public class DefaultModelProvider implements ModelProvider {
  private Model defaultCube;
  private Model defaultQuad;
  private Model testTerrain;

  @Override
  public Model getDefaultCube() {
    return defaultCube;
  }

  @Override
  public Model getDefaultQuad() {
    return defaultQuad;
  }

  @Override
  public Model getTestTerrain() {
    return testTerrain;
  }

  public void setDefaultCube(Model cube) {
    this.defaultCube = cube;
  }

  public void setTestTerrain(Model testTerrain) {
    this.testTerrain = testTerrain;
  }

  public void setDefaultQuad(Model defaultQuad) {
    this.defaultQuad = defaultQuad;
  }
}
