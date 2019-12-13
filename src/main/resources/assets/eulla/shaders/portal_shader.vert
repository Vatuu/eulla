#version 400 core

uniform mat4 mvp;

in vec3 pos;

out vec2 uv;

uniform mat4 screen2tex = mat4(
    0.5, 0, 0, 0,
    0, 0.5, 0, 0,
    0, 0, 0.5, 0,
    0.5, 0.5, 0.5, 1
);

void main() {
    vec4 p = mvp * vec4(pos, 1);
    vec4 p1 = screen2tex * p;
    uv = p1.xy / p1.w;
    gl_position = p;
}