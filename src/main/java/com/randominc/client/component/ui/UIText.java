package com.randominc.client.component.ui;

import com.randominc.client.engine.graphic.model.Mesh;
import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;

public class UIText extends EntityComponent {

  private String text;
  private int columns;
  private int rows;
  private Mesh mesh;

  public UIText(String text, int columns, int rows) {
    this.text = Objects.requireNonNull(text);
    this.columns = columns;
    this.rows = rows;
  }

  public String getText() {
    return text;
  }

  public int getColumns() {
    return columns;
  }

  public int getRows() {
    return rows;
  }

  @Override
  protected void removeComponent() {}

  public void setMesh(Mesh mesh) {
    this.mesh = Objects.requireNonNull(mesh);
  }

  public Mesh getMesh() {
    return mesh;
  }
}
