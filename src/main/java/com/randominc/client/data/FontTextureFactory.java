package com.randominc.client.data;

import com.randominc.client.engine.graphic.preprocessing.TextureFactory;
import com.randominc.client.engine.graphic.texture.Texture;
import com.randominc.client.engine.graphic.utility.FileUtil;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.imageio.ImageIO;

public class FontTextureFactory {

  private TextureFactory textureFactory;

  public FontTextureFactory(
      TextureFactory textureFactory) {
    this.textureFactory = Objects.requireNonNull(textureFactory);
  }

  public Texture createDefaultFrontTexture() {
    Font font = new Font(Font.MONOSPACED, Font.PLAIN, 12);
    Map charMap = new HashMap<>();
    String charSetName = "ISO-8859-1";

    // Get the font metrics for each character for the selected font by using image
    BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
    Graphics2D g2D = img.createGraphics();
    g2D.setFont(font);
    FontMetrics fontMetrics = g2D.getFontMetrics();

    String allChars = getAllAvailableChars(charSetName);
    int width = 0;
    int height = 0;
    for (char c : allChars.toCharArray()) {
      // Get the size for each character and update global image size
      CharInfo charInfo = new CharInfo(width, fontMetrics.charWidth(c));
      charMap.put(c, charInfo);
      width += charInfo.getWidth();
      height = Math.max(height, fontMetrics.getHeight());
    }
    g2D.dispose();

    // Create the image associated to the charset
    img = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    g2D = img.createGraphics();
    g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2D.setFont(font);
    fontMetrics = g2D.getFontMetrics();
    g2D.setColor(Color.WHITE);
    g2D.drawString(allChars, 0, fontMetrics.getAscent());
    g2D.dispose();

    // Dump image to a byte buffer
//    InputStream is;
//    try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
//      ImageIO.write(img, "png", out);
//      out.flush();
//      is = new ByteArrayInputStream(out.toByteArray());
//    } catch (IOException e) {
//      new DefaultDebugLogProvider().getDebugLog(this).error("Failed to create font Texture.", e);
//    }

    try {
      ImageIO.write(img, "png", new java.io.File("src/main/java/com/randominc/client/resources/texture/font/arial.png"));
    } catch (IOException e) {
      e.printStackTrace();
    }

    return textureFactory.createTexture("font/arial.png");
  }

  private String getAllAvailableChars(String charsetName) {
    CharsetEncoder ce = Charset.forName(charsetName).newEncoder();
    StringBuilder result = new StringBuilder();
    for (char c = 0; c < Character.MAX_VALUE; c++) {
      if (ce.canEncode(c)) {
        result.append(c);
      }
    }
    return result.toString();
  }

  public static class CharInfo {

    private final int startX;

    private final int width;

    public CharInfo(int startX, int width) {
      this.startX = startX;
      this.width = width;
    }

    public int getStartX() {
      return startX;
    }

    public int getWidth() {
      return width;
    }
  }
}
