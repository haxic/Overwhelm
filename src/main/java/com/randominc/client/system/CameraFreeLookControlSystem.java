package com.randominc.client.system;

import com.randominc.client.component.Camera;
import com.randominc.client.component.DirectionalMovement;
import com.randominc.client.engine.core.GameEngine;
import com.randominc.client.engine.window.KeyState;
import com.randominc.client.engine.window.WindowInputProvider;
import com.randominc.client.game.camera.CameraFreeLookAction;
import com.randominc.client.game.control.Binding;
import com.randominc.client.game.control.ControllerType;
import com.randominc.shared.hecs.Entity;
import com.randominc.shared.hecs.EntityManager;
import java.util.EnumMap;
import java.util.Objects;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.glfw.GLFW;

public class CameraFreeLookControlSystem {
  private final EntityManager entityManager;
  private final WindowInputProvider windowInputProvider;
  private final GameEngine gameEngine;
  private final EnumMap<CameraFreeLookAction, Binding> bindings;
  private static final Vector3i tempDirection = new Vector3i();
  private static final Vector3f tempRotation = new Vector3f();
  private final Vector2f center;
  private final Vector2f cursorOffset;
  private final Vector2f currentCursorPosition;
  private boolean inverseMouseYAxis;
  private float horizontalMouseSpeed;
  private float verticalMouseSpeed;

  public CameraFreeLookControlSystem(
      EntityManager entityManager, WindowInputProvider windowInputProvider, GameEngine gameEngine) {
    this.entityManager = Objects.requireNonNull(entityManager);
    this.windowInputProvider = Objects.requireNonNull(windowInputProvider);
    this.gameEngine = Objects.requireNonNull(gameEngine);
    center = new Vector2f();
    currentCursorPosition = new Vector2f();
    cursorOffset = new Vector2f();
    bindings = new EnumMap<>(CameraFreeLookAction.class);

    setMoveForward(ControllerType.KEY, GLFW.GLFW_KEY_W);
    setMoveBackward(ControllerType.KEY, GLFW.GLFW_KEY_S);
    setStrafeLeft(ControllerType.KEY, GLFW.GLFW_KEY_A);
    setStrafeRight(ControllerType.KEY, GLFW.GLFW_KEY_D);
    setAscend(ControllerType.KEY, GLFW.GLFW_KEY_LEFT_CONTROL);
    setDescend(ControllerType.KEY, GLFW.GLFW_KEY_LEFT_SHIFT);

    setPitchDown(ControllerType.KEY, GLFW.GLFW_KEY_DOWN);
    setPitchUp(ControllerType.KEY, GLFW.GLFW_KEY_UP);
    setYawLeft(ControllerType.KEY, GLFW.GLFW_KEY_LEFT);
    setYawRight(ControllerType.KEY, GLFW.GLFW_KEY_RIGHT);

    setExit(ControllerType.KEY, GLFW.GLFW_KEY_ESCAPE);
    setToggleFullscreen(ControllerType.KEY, GLFW.GLFW_KEY_F11);
    setToggleCursor(ControllerType.KEY, GLFW.GLFW_KEY_F12);

    setInverseMouseYAxis(true);
    setHorizontalMouseSpeed(0.25f);
    setVerticalMouseSpeed(0.25f);
  }

  // Movement
  public void setMoveForward(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.MOVE_FORWARD, new Binding(controllerType, binding));
  }

  public void setMoveBackward(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.MOVE_BACKWARD, new Binding(controllerType, binding));
  }

  public void setStrafeLeft(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.STRAFE_LEFT, new Binding(controllerType, binding));
  }

  public void setStrafeRight(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.STRAFE_RIGHT, new Binding(controllerType, binding));
  }

  public void setAscend(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.ASCEND, new Binding(controllerType, binding));
  }

  public void setDescend(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.DESCEND, new Binding(controllerType, binding));
  }

  // Rotation
  public void setPitchDown(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.PITCH_DOWN, new Binding(controllerType, binding));
  }

  public void setPitchUp(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.PITCH_UP, new Binding(controllerType, binding));
  }

  public void setYawLeft(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.YAW_LEFT, new Binding(controllerType, binding));
  }

  public void setYawRight(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.YAW_RIGHT, new Binding(controllerType, binding));
  }

  // Utility
  public void setExit(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.EXIT_GAME, new Binding(controllerType, binding));
  }

  public void setToggleFullscreen(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.TOGGLE_FULLSCREEN, new Binding(controllerType, binding));
  }

  public void setToggleCursor(ControllerType controllerType, int binding) {
    bindings.put(CameraFreeLookAction.TOGGLE_CURSOR, new Binding(controllerType, binding));
  }

  // Settings
  public void setInverseMouseYAxis(boolean inverseMouseYAxis) {
    this.inverseMouseYAxis = inverseMouseYAxis;
  }

  public void setHorizontalMouseSpeed(float horizontalMouseSpeed) {
    this.horizontalMouseSpeed = horizontalMouseSpeed;
  }

  public void setVerticalMouseSpeed(float verticalMouseSpeed) {
    this.verticalMouseSpeed = verticalMouseSpeed;
  }

  public void update() {
    tempDirection.set(0);
    tempRotation.set(0);
    Entity entity = entityManager.getEntityContainingComponentOfClass(Camera.class);
    DirectionalMovement directionalMovement =
        (DirectionalMovement) entityManager.getComponent(entity, DirectionalMovement.class);
    bindings.forEach(
        (action, binding) -> {
          KeyState keyState = null;
          switch (binding.getControllerType()) {
            case KEY:
              keyState = windowInputProvider.getKey(binding.getBinding());
              break;
            case MOUSE_BUTTON:
              keyState = windowInputProvider.getMouseButton(binding.getBinding());
              break;
            case SCROLL:
              keyState = windowInputProvider.getScrollYDirection(binding.getBinding());
              break;
            default:
              break;
          }
          if (KeyState.PRESSED == keyState) {
            handleContinuousActions(directionalMovement, action);
          }
          if (KeyState.CLICKED == keyState) {
            handleSingleActions(action);
          }
        });

    if (!gameEngine.isCursorShown()) {
      gameEngine.getCenter(center);
      windowInputProvider.getCursorPosition(currentCursorPosition);
      center.sub(currentCursorPosition, cursorOffset);

      float mouseYOffset =
          (inverseMouseYAxis ? -cursorOffset.y() : cursorOffset.y()) * verticalMouseSpeed;
      float mouseXOffset = cursorOffset.x() * horizontalMouseSpeed;
      directionalMovement.setRotation(mouseYOffset, mouseXOffset);
      gameEngine.centerCursor();
    }
  }

  private void handleSingleActions(CameraFreeLookAction action) {
    switch (action) {
      case EXIT_GAME:
        gameEngine.exitGame();
        break;
      case TOGGLE_FULLSCREEN:
        gameEngine.toggleFullscreen();
        break;
      case TOGGLE_CURSOR:
        gameEngine.toggleCursor();
        break;
      default:
        break;
    }
  }

  private void handleContinuousActions(
      DirectionalMovement directionalMovement, CameraFreeLookAction action) {
    switch (action) {
      case MOVE_FORWARD:
        tempDirection.y++;
        break;
      case MOVE_BACKWARD:
        tempDirection.y--;
        break;
      case STRAFE_LEFT:
        tempDirection.x--;
        break;
      case STRAFE_RIGHT:
        tempDirection.x++;
        break;
      case ASCEND:
        tempDirection.z++;
        break;
      case DESCEND:
        tempDirection.z--;
        break;

      case PITCH_DOWN:
        tempRotation.x--;
        break;
      case PITCH_UP:
        tempRotation.x++;
        break;
      case YAW_LEFT:
        tempRotation.z++;
        break;
      case YAW_RIGHT:
        tempRotation.z--;
        break;

      default:
        break;
    }
    directionalMovement.setDirection(tempDirection);
    directionalMovement.setRotation(tempRotation.x(), tempRotation.z());
  }
}
