#version 400 core

uniform mat4 mvp;

in vec3 pos;

out vec4 pos1;

void main() {
    gl_Position = pos1 = mvp * vec4(pos, 1);
}