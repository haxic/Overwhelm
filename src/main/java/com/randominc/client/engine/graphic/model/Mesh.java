package com.randominc.client.engine.graphic.model;

public class Mesh {
  private int vaoId;
  private int indicesSize;
  private int indexBufferVbo;

  public Mesh(int vaoId, int indicesSize) {
    this.vaoId = vaoId;
    this.indicesSize = indicesSize;
  }

  public int getVaoId() {
    return vaoId;
  }

  public int getIndicesSize() {
    return indicesSize;
  }

  public void setIndexBufferVbo(int indexBufferVbo) {
    this.indexBufferVbo = indexBufferVbo;
  }

  public int getIndexBufferVbo() {
    return indexBufferVbo;
  }
}
