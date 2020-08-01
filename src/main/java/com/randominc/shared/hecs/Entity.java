package com.randominc.shared.hecs;

import java.util.ArrayList;

public class Entity {
  protected final ArrayList<EntityContainer> references;
  private final long entityId;

  public Entity(long entityId) {
    this.entityId = entityId;
    references = new ArrayList<>();
  }

  public void attach(EntityContainer entityContainer) {
    references.add(entityContainer);
  }

  public void detach(EntityContainer entityContainer) {
    references.remove(entityContainer);
  }

  public long getEntityId() {
    return entityId;
  }

  @Override
  public String toString() {
    return "(" + entityId + ")";
  }
}
