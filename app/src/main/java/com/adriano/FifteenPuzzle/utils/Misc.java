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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Misc {
    public static FloatBuffer CreateFloatBuffer(int Size) {
        ByteBuffer BBuffer = ByteBuffer.allocateDirect(Size << 2).order(ByteOrder.nativeOrder());
        return BBuffer.asFloatBuffer();
    }

    public static IntBuffer CreateIntBuffer(int Size) {
        ByteBuffer BBuffer = ByteBuffer.allocateDirect(Size * 4).order(ByteOrder.nativeOrder());
        IntBuffer Result = BBuffer.asIntBuffer();
        return Result;
    }

    public static ByteBuffer CreateByteBuffer(int size) {
        return ByteBuffer.allocateDirect(size).order(ByteOrder.nativeOrder());
    }

}
