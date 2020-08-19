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
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;


public class ColorQuad extends Quad {
    protected Color4 Color;


    public void SetColor(Color4 Color) {
        this.Color = Color;
        UpdateData();
    }

    public Color4 GetColor() {
        return Color;
    }

    @Override
    public void UpdateData() {
        Vbo.UpdateVertices(new Rect(Vec2.ZeroVector(),GetRectangle().Width,GetRectangle().Height),Color);
    }

    public void Draw() {
        int MatrixID;

        super.Draw();

        GLES30.glUseProgram(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d));
        MatrixID = GLES30.glGetUniformLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                "OrthoMatrix");
        GLES30.glUniformMatrix4fv(MatrixID,1, false, RenderUtils.MVPMatrix,0);

        Vbo.Draw();
        GLES30.glUseProgram(0);

        RenderUtils.ResetModelMatrix();
    }

    public void Init(Vec2 Position,float Width,float Height,Color4 Color) {
        short[] Indices = {
                0, 1, 2,
                3, 0, 2
        };
        Vbo = new VBO();
        Vbo.InitXYRGBA(Width,Height,Color,Indices);
        RenderUtils.CacheShader(R.id.colorshader2d, R.raw.color2dvertex, R.raw.color2dfragment);
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                0, "vPosition");
        GLES30.glBindAttribLocation(RenderUtils.ShaderUtils.GetProgramIdByResourceID(R.id.colorshader2d),
                1, "in_ColourComponents");
        SetRectangle(Position, Width, Height);
    }

    public ColorQuad(RenderEngine RenderUtils, Vec2 Position,float Width,float Height, Color4 Color) {
        this.RenderUtils = RenderUtils;
        Init(Position,Width,Height,Color);
    }

    public ColorQuad(RenderEngine RenderUtils, Rect Rectangle, Color4 Color) {
        this.RenderUtils = RenderUtils;
        this.Color = Color;
        Init(Rectangle.Position,Rectangle.Width,Rectangle.Height,Color);
    }
}

