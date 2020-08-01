package com.randominc.client.system;

import com.randominc.client.component.Direction;
import com.randominc.client.component.DirectionalMovement;
import com.randominc.client.component.Position;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.List;
import org.joml.Vector3f;
import org.joml.Vector3i;

public class DirectionalMovementSystem {

  private final EntityManager entityManager;
  private static final Vector3f tempPosition = new Vector3f();
  private static final Vector3f tempMovementVector = new Vector3f();

  public DirectionalMovementSystem(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  public void update(float delta) {
    List<Entity> entities = entityManager.getEntitiesContainingComponent(DirectionalMovement.class);
    if (entities == null) return;
    if (entities.isEmpty()) return;
    for (Entity entity : entities) {
      Position position = (Position) entityManager.getComponent(entity, Position.class);
      Direction direction = (Direction) entityManager.getComponent(entity, Direction.class);
      DirectionalMovement directionalMovement =
          (DirectionalMovement) entityManager.getComponent(entity, DirectionalMovement.class);
      if (position == null || direction == null || directionalMovement == null) {
        continue;
      }
      // Get current direction and position
      Vector3i directionalMovementVector = directionalMovement.getDirection();
      float forward = directionalMovementVector.y();
      float horizontal = directionalMovementVector.x();
      float vertical = directionalMovementVector.z();

      float pitch = directionalMovement.getPitch();
      float yaw = directionalMovement.getYaw();

      tempPosition.set(position.getPosition());
      tempMovementVector.set(0);

      Vector3f forwardVector = direction.getForward();
      Vector3f horizontalVector = direction.getRight();

      tempMovementVector.fma(forward * delta * 0.01f, forwardVector);
      tempMovementVector.fma(horizontal * delta * 0.01f, horizontalVector);
      tempMovementVector.add(0, 0, vertical * delta * 0.01f);

      direction.rotateAroundZAxis(yaw * 0.01f);
      direction.rotateAroundRightAxis(pitch * 0.01f);
      position.setPosition(tempPosition.add(tempMovementVector));
      // Reset directional movement
      directionalMovement.reset();
    }
  }
}
