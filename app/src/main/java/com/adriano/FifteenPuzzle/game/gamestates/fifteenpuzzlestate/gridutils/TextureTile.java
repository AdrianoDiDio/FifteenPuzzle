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


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.TextureQuad;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.ContextUtils;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class TextureTile extends GridTile {
    private TextureQuad Quad;

    @Override
    public void SetNumber(int Number) {
        super.SetNumber(Number);
        Quad.SetTextureID(RenderUtils.TextureUtils.GetTextureID("GridNumber" + Number));
    }

    @Override
    public void SetRect(Rect Rectangle) {
        super.SetRect(Rectangle);
        Quad.SetRectangle(Rectangle);
        //If not the dimension of the quad won't be updated.
        Quad.UpdateData();
    }

    @Override
    public void SetPosition(Vec2 NewPosition) {
        super.SetPosition(NewPosition);
        Quad.SetPosition(NewPosition);

    }
    float LerpTime;
    @Override
    public void Move(Vec2 Start, Vec2 End) {
        this.StartPosition = new Vec2(Start);
        this.EndPosition = new Vec2(End);
        this.MovementDelta = EndPosition.SubtractFrom(StartPosition);
        this.MovementDelta.Normalize();
        this.MovementLength = StartPosition.SubtractFrom(EndPosition).Length();
        this.MovementStartTime = RenderEngine.SysMilliseconds();
        LerpTime = 0.f;
        Speed = 1f;
        IsMoving = true;
    }

    @Override
    protected void UpdatePosition() {
        Vec2 Offset;
        float DistCovered;
        float Fraction;

        if( !IsMoving ) {
            return;
        }
        if( GetRect().Position.x == EndPosition.x &&
                GetRect().Position.y == EndPosition.y ) {
            IsMoving = false;
            return;
        }
        //Advance the animation frame using lerp?
        //Translate the Rectangle!
        DistCovered = (RenderEngine.SysMilliseconds() - MovementStartTime) * Speed;
        Fraction = DistCovered / MovementLength;
        Offset = Vec2.Lerp(StartPosition,EndPosition,Fraction);
        SetPosition(Offset);
    }

    @Override
    public void Draw() {
        if( IsMoving ) {
            UpdatePosition();
        }
        Quad.Draw();
    }

    private void Init() {
        int TextureID;
        Bitmap Image;
        String GridNumber;

        if( !RenderUtils.TextureUtils.Exists("GridNumber"+Number) ) {
            GridNumber = String.format(
                    ContextUtils.GetStringFromResources(RenderUtils.AndroidContext,
                            R.string.GridImageFilePattern),Number);
            Image = BitmapFactory.decodeStream(RenderUtils.Assets.Get(GridNumber).
                    GetInputStream());
            TextureID = RenderUtils.TextureUtils.Cache("GridNumber" + Number,Image);
        } else {
            TextureID = RenderUtils.TextureUtils.GetTextureID("GridNumber" + Number);
        }
        Quad = new TextureQuad(RenderUtils,TextureID, GetRect(),TexCoord.SimpleQuad);
    }

    public TextureTile(RenderEngine RenderUtils, int Number, Rect Position) {
        super(RenderUtils,Number,Position);
        Init();
    }
}
