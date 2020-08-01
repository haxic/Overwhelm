#version 330 core

in vec3 position;

uniform mat4 mvp;

out vec3 color;

void main() {
  gl_Position = mvp * vec4(position, 1.0);
  color = vec3(position);
}