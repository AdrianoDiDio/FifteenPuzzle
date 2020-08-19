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
package com.adriano.FifteenPuzzle.utils;

public class Rect {
    public Vec2 Position;
    public float Width;
    public float Height;

    public void Translate(Vec2 TranslationOffset) {
        Position.Add(TranslationOffset);
    }

    public boolean Inside(float x,float y) {
        if( x >= Position.x && y >= Position.y &&
            x <= (Position.x + Width) && y <= (Position.y + Height)) {
            return true;
        }
        return false;
    }

    public boolean Inside(Vec2 Position) {
        return Inside(Position.x,Position.y);
    }

    public Rect(Vec2 Position,float Width,float Height) {
        this.Position = new Vec2(Position);
        this.Width = Width;
        this.Height = Height;
    }
    public Rect(Rect Other) {
        this.Position = new Vec2(Other.Position);
        this.Width = Other.Width;
        this.Height = Other.Height;
    }
    public Rect() {
        Position = new Vec2();
        Width = 0;
        Height = 0;
    }
}
