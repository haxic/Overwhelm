package com.randominc.client.engine.window;

import org.joml.Vector2f;

public interface WindowInputProvider {

  KeyState getKey(int key);

  KeyState getMouseButton(int mouseButton);

  KeyState getScrollYDirection(int binding);

  Vector2f getCursorPosition(Vector2f destination);

  Vector2f getCursorDelta(Vector2f destination);
}
