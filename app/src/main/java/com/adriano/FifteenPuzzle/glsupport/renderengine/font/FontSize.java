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

import timber.log.Timber;

public enum FontSize {
    FONT_SMALL(0),
    FONT_MEDIUM(1),
    FONT_BIG(2);

    public int Value;

    public static FontSize Get(String Value) {
        switch ( Value ) {
            case "AFont":
                return FONT_SMALL;
            case "AFontMedium":
                return FONT_MEDIUM;
            case "AFontBig":
                return FONT_BIG;
            default:
                Timber.d("Unknown Value in enum " + Value);
                break;
        }
        return FONT_SMALL;
    }

    FontSize(int Value) {
        this.Value = Value;
    }
}
