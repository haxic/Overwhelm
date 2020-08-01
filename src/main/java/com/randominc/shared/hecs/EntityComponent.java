package com.randominc.shared.hecs;

public abstract class EntityComponent {

  /**
   * This method is called when the container is a component that is being removed by the entity
   * manager. It must detach all entities that it references to.
   */
  protected abstract void removeComponent();
}
