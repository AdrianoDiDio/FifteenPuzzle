/*
===========================================================================
    Copyright (C) 2019 Adriano Di Dio.
    
    FifteenPuzzle is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    FifteenPuzzle is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with FifteenPuzzle.  If not, see <http://www.gnu.org/licenses/>.
===========================================================================
*/ 
package com.adriano.FifteenPuzzle.glsupport;

import android.opengl.GLES30;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class Line extends Shape {
    protected Color4 Color;
    protected Vec2 End;

    public void setColor(Color4 Color) {
        this.Color = Color;
    }

    public Color4 getColor() {
        return Color;
    }

    public void SetEnd(Vec2 End) {
        this.End = End;
    }

    public Vec2 getEnd() {
        return End;
    }

    private float[] GetVertexArray(Vec2 Start, Vec2 End, Color4 Color) {
        float[] Vertices = {
                Start.x,Start.y,
                Color.r,Color.g,Color.b,Color.a,
                End.x,End.y,
                Color.r,Color.g,Color.b,Color.a,
        };
        return Vertices;
    }

    public void SetPosition(Vec2 Start, Vec2 End, Color4 Color) {
        super.SetPosition(Start);
        this.End = End;
        Vbo.UpdateVertices(GetVertexArray(Start,End,Color));
    }

    @Override
    public void UpdateData() {
        Vbo.UpdateVertices(Position,End,Color);
    }

    public void Draw() {
        //Finally Draw it.
        GLES30.glUseProgram(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d));

        //TODO:Robustness...Instead of querying it here get the shader and then do
        //FontShader.GetUniform("Foo") => If not found warn the user.
        int MatrixID = GLES30.glGetUniformLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                "OrthoMatrix");
        GLES30.glUniformMatrix4fv(MatrixID,1, false, RenderUtils.MVPMatrix,0);

        Vbo.Draw(GLES30.GL_LINES);
        GLES30.glUseProgram(0);
    }

    public void Init(Vec2 Start,Vec2 End,Color4 Color) {
        short[] Indices = {
                0, 1
        };
        Vbo = new VBO();
        Vbo.InitXYRGBA(GetVertexArray(Start,End,Color), Indices);

        RenderUtils.CacheShader(R.id.colorshader2d, R.raw.color2dvertex, R.raw.color2dfragment);

        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                0, "vPosition");
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                1, "in_ColourComponents");

    }

    public Line(RenderEngine RenderUtils,Vec2 Start,Vec2 End,Color4 Color) {
        this.RenderUtils = RenderUtils;
        this.End = End;
        this.Color = Color;
        Init(Start,End,Color);
    }
}
