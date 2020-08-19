#version 300 es

in vec2 in_Position;
in vec2 in_TextureCoord;
uniform vec4 in_TextureColor;
uniform mat4 OrthoMatrix;

out vec2 pass_TextureCoord;
out vec4 TextureColor;

void main(void) {
	gl_Position = OrthoMatrix * vec4(in_Position,0,1);
	
	pass_TextureCoord = in_TextureCoord;
	
	//TextureColor = in_TextureColor;
	TextureColor = in_TextureColor;
}