#version 120

uniform sampler2D sample;

varying vec2 texCoords;

void main() {
    gl_FragColor = texture2D(sample, texCoords);
}