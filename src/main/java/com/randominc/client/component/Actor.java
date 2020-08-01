package com.randominc.client.component;

import com.randominc.client.engine.graphic.model.Model;
import com.randominc.shared.hecs.EntityComponent;
import java.util.Objects;

public class Actor extends EntityComponent {

  private Model model;

  public Actor(Model model) {
    this.model = Objects.requireNonNull(model);
  }

  public Model getModel() {
    return model;
  }

  @Override
  protected void removeComponent() {}
}
