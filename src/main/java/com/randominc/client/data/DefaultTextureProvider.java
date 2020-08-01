package com.randominc.client.data;

import com.randominc.client.engine.graphic.texture.Texture;

public class DefaultTextureProvider implements TextureProvider {
  private Texture defaultFontTexture;

  @Override
  public Texture getDefaultFontTexture() {
    return defaultFontTexture;
  }

  public void setDefaultFontTexture(Texture defaultFontTexture) {
    this.defaultFontTexture = defaultFontTexture;
  }
}
