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
package com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.gridutils;

import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

public abstract class GridTile {
    protected RenderEngine RenderUtils;
    private Rect Rectangle;
    protected int  Number;
    //Movement specific code.
    protected Vec2 StartPosition;
    protected Vec2 EndPosition;
    protected Vec2 MovementDelta;
    protected float MovementLength;
    protected float MovementStartTime;
    protected float Speed;
    protected boolean IsMoving;

    public abstract void Move(Vec2 Start,Vec2 End);

    public int GetNumber() {
        return Number;
    }

    public void SetNumber(int Number) {
        this.Number = Number;
    }

    public void SetRect(Rect Position) {
        this.Rectangle = new Rect(Position);
    }

    public Rect GetRect() {
        return Rectangle;
    }

    public Vec2 GetPosition() {
        return  Rectangle.Position;
    }

    public void SetPosition(Vec2 Position) {
        GetRect().Position = new Vec2(Position);
    }

    protected abstract void UpdatePosition();

    public abstract void Draw();



    public GridTile(RenderEngine RenderUtils,int Number, Rect Position) {
        this.RenderUtils = RenderUtils;
        this.Number = Number;
        this.Rectangle = new Rect(Position);
        this.IsMoving = false;
    }

}
