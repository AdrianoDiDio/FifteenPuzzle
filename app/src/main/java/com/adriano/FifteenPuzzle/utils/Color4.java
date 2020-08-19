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

public class Color4 {
    public float r;
    public float g;
    public float b;
    public float a;

    public static Color4 Red = new Color4(255.f,0.f,0.f);
    public static Color4 Green = new Color4(0.f,255.f,0.f);
    public static Color4 Blue = new Color4(0.f,0.f,255.f);
    public static Color4 Yellow = new Color4(255.f,255.f,0.f);
    public static Color4 White = new Color4(255.f,255.f,255.f);
    public static Color4 Black = new Color4(0.f,0.f,0.f);
    public static Color4 LightBlue = new Color4(102.f,193.f,248.f,125.f);
    public static Color4 TransparentWhite = new Color4(255.f,255.f,255.f,0.f);

    public float ClampTo01(float InColor) {
        if( InColor >= 0 && InColor <= 1 ) {
            return InColor;
        }
        return InColor / 255.f;
    }

    public Color4(float r,float g,float b,float a) {
        this.r = ClampTo01(r);
        this.g = ClampTo01(g);
        this.b = ClampTo01(b);
        this.a = ClampTo01(a);
    }

    public Color4(float r,float g,float b) {
        this.r = ClampTo01(r);
        this.g = ClampTo01(g);
        this.b = ClampTo01(b);
        this.a = 1.f;
    }

    public Color4() {
        r = g = b = a = 125.f;
    }
}
