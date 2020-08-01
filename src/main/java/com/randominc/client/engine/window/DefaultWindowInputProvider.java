package com.randominc.client.engine.window;

import com.randominc.shared.debug.DebugLog;
import com.randominc.shared.debug.DefaultDebugLogProvider;
import java.util.HashMap;
import java.util.Map;
import org.joml.Vector2f;
import org.joml.Vector2i;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class DefaultWindowInputProvider implements WindowInputProvider {

  private final DebugLog debugLog;
  private final long inputWindow;
  private final Map<Integer, KeyState> keys = new HashMap<>();
  private final Map<Integer, KeyState> mouseButtons = new HashMap<>();
  private final Map<Integer, KeyState> scrollYDirections = new HashMap<>();
  private final Map<Integer, KeyState> tempPollMap = new HashMap<>();
  private final Vector2i scrollOffset;
  private final Vector2i cursorPos;

  private final GLFWKeyCallback keyCallback;
  private final GLFWMouseButtonCallback mouseButtonCallback;
  private final GLFWScrollCallback scrollCallback;
  private final GLFWCursorPosCallback cursorPosCallback;

  public DefaultWindowInputProvider(long inputWindow) {
    debugLog = new DefaultDebugLogProvider().getDebugLog(this);
    this.inputWindow = inputWindow;
    cursorPos = new Vector2i();
    scrollOffset = new Vector2i();

    keyCallback =
        new GLFWKeyCallback() {
          @Override
          public synchronized void invoke(
              long window, int key, int scancode, int action, int mods) {
            if (inputWindow == window) {
              KeyState keyState;
              if (action == GLFW.GLFW_RELEASE) {
                keyState = KeyState.RELEASED;
              } else {
                boolean wasPressed = keys.get(key) == KeyState.PRESSED;
                keyState = wasPressed ? KeyState.PRESSED : KeyState.CLICKED;
              }
              keys.put(key, keyState);
              debugLog.debug(
                  String.format(
                      "Key [%d] invoked. Scan code: [%d]. Key name: []. Key state: [%s].",
                      key, scancode, keyState.toString()));
            }
          }
        };

    mouseButtonCallback =
        new GLFWMouseButtonCallback() {
          @Override
          public synchronized void invoke(long window, int button, int action, int mods) {
            if (inputWindow == window) {
              KeyState keyState;
              if (action == GLFW.GLFW_RELEASE) {
                keyState = KeyState.RELEASED;
              } else {
                boolean wasPressed = mouseButtons.get(button) == KeyState.PRESSED;
                keyState = wasPressed ? KeyState.PRESSED : KeyState.CLICKED;
              }
              mouseButtons.put(button, keyState);
              debugLog.debug(
                  String.format(
                      "Mouse button [%d] invoked, key state: [%s]", button, keyState.toString()));
            }
          }
        };

    scrollCallback =
        new GLFWScrollCallback() {
          @Override
          public void invoke(long window, double xoffset, double yoffset) {
            if (DefaultWindowInputProvider.this.inputWindow == window) {
              scrollOffset.set((int) xoffset, (int) yoffset);

              int scrollDirection = -1;
              if (yoffset > 0) {
                scrollDirection = 1;
              } else if (yoffset < 0) {
                scrollDirection = 0;
              }
              KeyState keyState = yoffset != -1 ? KeyState.CLICKED : KeyState.RELEASED;
              scrollYDirections.put(scrollDirection, keyState);

              debugLog.debug(
                  String.format("Scroll invoked, offsets: [%d:%d]", (int) xoffset, (int) yoffset));
            }
          }
        };

    cursorPosCallback =
        new GLFWCursorPosCallback() {
          @Override
          public synchronized void invoke(long window, double xpos, double ypos) {
            if (DefaultWindowInputProvider.this.inputWindow == window) {
              cursorPos.set((int) xpos, (int) ypos);
            }
          }
        };
  }

  public GLFWKeyCallback getKeyCallback() {
    return keyCallback;
  }

  public GLFWMouseButtonCallback getMouseButtonCallback() {
    return mouseButtonCallback;
  }

  public GLFWScrollCallback getScrollCallback() {
    return scrollCallback;
  }

  public GLFWCursorPosCallback getCursorPosCallback() {
    return cursorPosCallback;
  }

  public void update() {
    tempPollMap.clear();
    keys.forEach(
        (integer, keyState) -> {
          if (keyState == KeyState.CLICKED) {
            tempPollMap.put(integer, KeyState.PRESSED);
          }
        });
    tempPollMap.forEach(keys::put);

    tempPollMap.clear();
    mouseButtons.forEach(
        (integer, keyState) -> {
          if (keyState == KeyState.CLICKED) {
            tempPollMap.put(integer, KeyState.PRESSED);
          }
        });
    tempPollMap.forEach(mouseButtons::put);

    tempPollMap.clear();
    scrollYDirections.forEach(
        (integer, keyState) -> {
          if (keyState == KeyState.CLICKED) {
            tempPollMap.put(integer, KeyState.PRESSED);
          }
        });
    tempPollMap.forEach(scrollYDirections::put);
  }

  public void cleanUp() {
    keyCallback.free();
    mouseButtonCallback.free();
    cursorPosCallback.free();
  }

  @Override
  public synchronized KeyState getKey(int key) {
    if (key >= GLFW.GLFW_KEY_LAST || key < 0) {
      debugLog.error(
          String.format(
              "Requested key [%s] was out of bounds. Must be within the range [0 to %d].",
              key, GLFW.GLFW_KEY_LAST));
      return KeyState.RELEASED;
    }
    return keys.getOrDefault(key, KeyState.RELEASED);
  }

  @Override
  public synchronized KeyState getMouseButton(int mouseButton) {
    if (mouseButton >= GLFW.GLFW_MOUSE_BUTTON_LAST || mouseButton < 0) {
      debugLog.error(
          String.format(
              "Requested mouse button [%s] was out of bounds. Must be within the range [0 to %d].",
              mouseButton, GLFW.GLFW_MOUSE_BUTTON_LAST));
      return KeyState.RELEASED;
    }
    return mouseButtons.getOrDefault(mouseButton, KeyState.RELEASED);
  }

  @Override
  public KeyState getScrollYDirection(int scrollYDirection) {
    if (scrollYDirection > 1 || scrollYDirection < 0) {
      debugLog.error(
          String.format(
              "Requested scroll [%s] was out of bounds. Must be within the range [0 to 1].",
              scrollYDirection));
      return KeyState.RELEASED;
    }
    return scrollYDirections.getOrDefault(scrollYDirection, KeyState.RELEASED);
  }

  @Override
  public synchronized Vector2f getCursorPosition(Vector2f destination) {
    return destination.set(cursorPos);
  }
}
