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
package com.adriano.FifteenPuzzle.glsupport.renderengine.font;

import android.graphics.Bitmap;

public class FontInfo {
    public	String Name;
    public	int Width;
    public	int Height;
    public	int CWidth;
    public	int CHeight;
    public  int LineHeight;
    public	int FSize;
    public Bitmap Image;
    public	CharInfo[] Character = new CharInfo[256];

    public int GetStringWidth(String Text) {
        char c;
        int Width;
        int i;

        Width = 0;

        if( Text == null || Text.isEmpty() ){
            return 0;
        }

        for( i = 0; i < Text.length(); i++ ) {
            c = Text.charAt(i);
            //TODO:Broken if a new line char is passed.
            if( c == '\n') {
                continue;
            }
            Width += Character[c].AdvanceX;
        }
        return Width;
    }

    public int GetStringHeight(String Text) {
        return LineHeight;
        /*
        char c;
        int MaxHeight;
        int i;

        MaxHeight = 0;

        if( Text == null || Text.isEmpty() ){
            return 0;
        }

        for( i = 0; i < Text.length(); i++ ) {
            c = Text.charAt(i);
            if( MaxHeight < Character[c].GlyphHeight ) {
                MaxHeight = Character[c].GlyphHeight;
            }
        }
        return MaxHeight;*/
    }


    @Override public String toString() {
        StringBuilder Result = new StringBuilder();

        Result.append(this.getClass().getName() + " Object {\n");
        Result.append(" Name: " + Name + "\n");
        Result.append(" FontManager Size: " + FSize + "\n");
        Result.append("Texture Width: " + Width + "\n");
        Result.append("Texture Height: " + Height + "\n");
        Result.append("Character Width: " + CWidth + "\n");
        Result.append("Character Height: " + CHeight + "\n");
        Result.append("}");

        return Result.toString();
    }
}
