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

public class TexCoord {
    public float u;
    public float v;
    public float Width;
    public float Height;

    public static TexCoord SimpleQuad = new TexCoord(0,0,1,1);

    public TexCoord(float u,float v,float Width,float Height) {
        this.u = u;
        this.v = v;
        this.Width = Width;
        this.Height = Height;
    }
    public TexCoord() {
        this.u = 0;
        this.v = 0;
        this.Width = 0;
        this.Height = 0;
    }
}
