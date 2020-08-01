package com.randominc.client.engine.graphic.render;

import com.randominc.client.engine.graphic.model.Model;
import org.joml.Vector3f;

public class Terrain {

  private Model model;
  private Vector3f position = new Vector3f(0, 0, 0);

  public Terrain(Model model) {
    this.model = model;
  }

  public Model getModel() {
    return model;
  }

  public Vector3f getPosition() {
    return position;
  }
}
