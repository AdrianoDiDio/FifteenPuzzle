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

import android.opengl.Matrix;

import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

public abstract class Quad extends Shape {
    float Width;
    float Height;

    public void SetRectangle(Rect Rectangle) {
        this.Position = new Vec2(Rectangle.Position);
        this.Width = Rectangle.Width;
        this.Height = Rectangle.Height;
    }

    public void SetRectangle(Vec2 Position,float Width,float Height) {
        this.Position = new Vec2(Position);
        this.Width = Width;
        this.Height = Height;
    }

    public Rect GetRectangle() {
        return new Rect(Position,Width,Height);
    }

    public void Draw() {
        float[] TranslationMatrix;
        TranslationMatrix = new float[16];
        Matrix.setIdentityM(TranslationMatrix,0);
        Matrix.translateM(TranslationMatrix,0,Position.x,Position.y,0);
        RenderUtils.ModelMatrix = TranslationMatrix;
        RenderUtils.UpdateMVPMatrix();
    }
}
