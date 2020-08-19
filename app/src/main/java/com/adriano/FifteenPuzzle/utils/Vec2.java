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


/**
 * @author Adriano di Dio
 */
public class Vec2 {
    public float x;
    public float y;

    public static Vec2 ZeroVector() {
        return new Vec2(0,0);
    }
    public static Vec2 Lerp(Vec2 Start,Vec2 End,float Time) {
        Vec2 Result;
        Result = new Vec2();
        Result.x = Math.Lerp(Start.x,End.x,Time);
        Result.y = Math.Lerp(Start.y,End.y,Time);
        return Result;
    }
    /**
     * Helpful when you need to print it's content easily.
     * @see java.lang.Object#toString()
     */
    public String toString() {
        String Result;
        Result = x + ";" + y;
        return Result;
    }

    /**
     *
     */
    public void Negate() {
        x = -x;
        y = -y;
        return;
    }

    /**
     * @param Amount
     */
    public void Scale(float Amount) {
        x *= Amount;
        y *= Amount;
    }

    /**
     *
     * @return Vector Length NOT-SQUARED.
     */
    public float Length() {
        float Result = (float) java.lang.Math.sqrt(x*x + y*y);//(float) Math.sqrt((Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2)));
        return Result;
    }

    public void Normalize() {
        float Length = Length();
        if( Length == 0 || java.lang.Math.abs(Length) < 0.001 ){
            return;
        }
        x /= Length();
        y /= Length();
    }

    /**
     * @param Other
     * @return
     */
    public float DotProduct(Vec2 Other) {
        return x * Other.x + y * Other.y;
    }



    /**
     * @param Other to add this with.
     * @return
     */
    public void Add(Vec2 Other) {
        this.x += Other.x;
        this.y += Other.y;
        return;
    }

    /**
     * @param Amount to add this with.
     * @return
     */
    public void Add(float Amount) {
        this.x += Amount;
        this.y += Amount;
        return;
    }

    /**
     * @param Other to add this with.
     * @return A *NEW* Vec2 with the results.
     */
    public Vec2 AddFrom(Vec2 Other) {
        Vec2 Result = new Vec2();
        Result.x = x + Other.x;
        Result.y = y + Other.y;
        return Result;
    }
    /**
     * @param Amount to add this with.
     * @return A *NEW* Vec2 with the results.
     */
    public Vec2 AddFrom(float Amount) {
        Vec2 Result = new Vec2();
        Result.x = x + Amount;
        Result.y = y + Amount;
        return Result;
    }
    /**
     * @param Other to subtract this with.
     * @return
     */
    public void Subtract(Vec2 Other) {
        this.x -= Other.x;
        this.y -= Other.y;
        return;
    }

    /**
     * @param Other to subtract this with.
     * @return A new Vector with the stored results.
     */
    public Vec2 SubtractFrom(Vec2 Other) {
        Vec2 Result = new Vec2();
        Result.x = x - Other.x;
        Result.y = y - Other.y;
        return Result;
    }

    public Vec2 SubtractFrom(float Amount) {
        Vec2 Result = new Vec2();
        Result.x = x - Amount;
        Result.y = y - Amount;
        return Result;
    }
    public void InitFrom(float x, float y) {
        this.x = x;
        this.y = y;
    }
    /**
     *
     */
    public void Init() {
        InitFrom(0, 0);
    }

    public Vec2(Vec2 Other) {
        this.x = Other.x;
        this.y = Other.y;
    }

    public Vec2(float x,float y) {
        this.x = x;
        this.y = y;
    }

    public Vec2() {
        Init();
    }
}
