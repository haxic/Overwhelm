package com.randominc.client.engine.graphic.texture;

public class Texture {

  public static final Texture EMPTY = new Texture(-1);
  private final int textureID;

  public Texture(int textureID) {
    this.textureID = textureID;
  }

  public float getWidth() {
    return 0;
  }

  public float getHeight() {
    return 0;
  }

  public int getTextureID() {
    return textureID;
  }
}
