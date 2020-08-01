package com.randominc.client.data;

import com.randominc.client.engine.graphic.model.Mesh;

public interface MeshProvider {

  Mesh getQuad();

  Mesh getCube();
}
