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

public class Math {

    public static float Clamp01(float Number) {
        if( Number < 0.f ) {
            return 0.f;
        }
        if( Number > 1.f ) {
            return 1.f;
        }
        return Number;
    }

    /*
    * Return the linear interpolation between a and b
    * making sure Time is within the range
    * 0 1.
    * */
    public static float Lerp(float a,float b,float Time) {
        //return (1 - Time) * a + Time * b;
        float ClampedTime;
        ClampedTime = Clamp01(Time);
        return a + ( b - a ) * ClampedTime;
    }
}
