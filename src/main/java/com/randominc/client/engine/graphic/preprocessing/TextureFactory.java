package com.randominc.client.engine.graphic.preprocessing;

import com.randominc.client.engine.graphic.texture.Texture;
import com.randominc.client.engine.graphic.utility.FileUtil;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.IntBuffer;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;
import org.lwjgl.opengl.GL30;

public class TextureFactory {

  public Texture createTexture(String textureFilePath) {
    int[] pixels = null;
    int width = 0;
    int height = 0;
    try {

      URL source = getClass().getClassLoader().getResource("texture/" + textureFilePath);
      BufferedImage image = ImageIO.read(source);
      width = image.getWidth();
      height = image.getHeight();
      pixels = new int[width * height];
      image.getRGB(0, 0, width, height, pixels, 0, width);
    } catch (IOException e) {
      e.printStackTrace();
    }

    int[] data = new int[width * height];
    for (int i = 0; i < width * height; i++) {
      int a = (pixels[i] & 0xff000000) >> 24;
      int r = (pixels[i] & 0xff0000) >> 16;
      int g = (pixels[i] & 0xff00) >> 8;
      int b = (pixels[i] & 0xff);

      data[i] = a << 24 | b << 16 | g << 8 | r;
    }

    int textureID = GL11.glGenTextures();
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);
    new DefaultDebugLogProvider().getDebugLog(this).info(width + " " +  height);
    GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, getIntBuffer(data));
    GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
    GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL14.GL_TEXTURE_LOD_BIAS, 0f);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_SMOOTH);
    GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_SMOOTH);
    GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
    return new Texture(textureID);
  }

  private IntBuffer getIntBuffer(int[] data) {
    IntBuffer intBuffer = BufferUtils.createIntBuffer(data.length);
    intBuffer.put(data);
    intBuffer.flip();
    return intBuffer;
  }
}
