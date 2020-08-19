#version 300 es

in vec2 vPosition;
in vec4 in_ColourComponents;
uniform mat4 OrthoMatrix;
out vec4 pass_ColourComponents;

void main() {
	gl_Position = OrthoMatrix * vec4(vPosition.x,vPosition.y,0,1);
	pass_ColourComponents = in_ColourComponents;

}
