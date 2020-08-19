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

public class HoleTile extends GridTile {

    @Override
    public int GetNumber() {
        return super.GetNumber();
    }

    @Override
    public void Move(Vec2 Start, Vec2 End) {
        this.EndPosition = new Vec2(End);
        this.IsMoving = true;
    }

    @Override
    protected void UpdatePosition() {
        SetPosition(EndPosition);
    }

    @Override
    public void Draw() {
        if( IsMoving ) {
            UpdatePosition();
            return;
        }
        return;
    }

    public HoleTile(RenderEngine RenderUtils, int Number, Rect Position) {
        super(RenderUtils,Number,Position);
    }
}
