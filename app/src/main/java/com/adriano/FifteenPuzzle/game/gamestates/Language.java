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
package com.adriano.FifteenPuzzle.game.gamestates;

import com.adriano.FifteenPuzzle.glsupport.TextureQuad;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class Language {
    public String Name;
    public String CountryCode;
    public int FlagResourceID;
    public TextureQuad Quad;
    public Rect Rectangle;

    public boolean TouchInside(float x,float y) {
        return Rectangle.Inside(x,y);
    }

    public void UpdatePosition(Vec2 Position,float Width,float Height) {
        Rectangle.Position = Position;
        Rectangle.Width = Width;
        Rectangle.Height = Height;
        Quad.SetPosition(Position);
    }

    public void UpdatePosition(Rect Rectangle) {
        this.Rectangle = Rectangle;
        Quad.SetPosition(Rectangle.Position);
    }

    public Language(String Name,String countryCode,int FlagResourceID) {
        this.Name = Name;
        this.CountryCode = CountryCode;
        this.FlagResourceID = FlagResourceID;
        this.Rectangle = new Rect();
    }
}
