#version 300 es

precision mediump float;
in vec4 pass_ColourComponents;
//out vec4 out_Color;
uniform vec4 vColor;
out vec4 FragColor;
void main() {
	FragColor = pass_ColourComponents;
}