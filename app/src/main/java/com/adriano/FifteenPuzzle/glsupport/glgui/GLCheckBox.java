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
package com.adriano.FifteenPuzzle.glsupport.glgui;

import android.telecom.Call;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.TextureQuad;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class GLCheckBox extends GLWidget {
    private TextureQuad Quad;
    private int CheckBoxOffTextureID;
    private int CheckBoxOnTextureID;
    private String Text;
    private GLString TextLabel;
    private FontSize TextSize;
    private Color4 TextColor;
    private boolean Checked;
    private float CheckBoxDimension;
    private GLWidgetCheckBoxChangeCallback Callback;

    private boolean IsChecked() {
        return Checked;
    }

    private int GetCurrentTextureID() {
        if( Checked ) {
            return CheckBoxOnTextureID;
        }
        return CheckBoxOffTextureID;
    }
    private Vec2 GetCheckBoxPosition() {
        Vec2 Result;
        Result = new Vec2(Rectangle.Position);
        Result.x += (Rectangle.Width - CheckBoxDimension);
        Result.y += (Rectangle.Height - CheckBoxDimension) / 2;
        return Result;
    }

    @Override
    public void SetRectangle(Rect Rectangle) {
        super.SetRectangle(Rectangle);
        TextLabel.SetContainer(Rectangle);
        Quad.SetPosition(GetCheckBoxPosition());
    }

    @Override
    public boolean OnTouch(Vec2 Position) {
        if( Rectangle.Inside(Position) ) {
            Checked = !Checked;
            Quad.SetTextureID(GetCurrentTextureID());
            Callback.OnChange(new GLClickEvent(ID,ParentID),Checked);
            return true;
        }
        return false;
    }

    @Override
    public void Draw() {
        Quad.Draw();
        TextLabel.Draw();
    }

    private void Init() {
        Rect CheckBoxRect;
        CheckBoxOffTextureID = RenderUtils.TextureUtils.Cache("CheckBoxOff", R.raw.checkboxoff);
        CheckBoxOnTextureID = RenderUtils.TextureUtils.Cache("CheckBoxOn", R.raw.checkboxon);
        //TODO:Hardcoded...
        CheckBoxDimension = 72;
        CheckBoxRect = new Rect(GetCheckBoxPosition(),CheckBoxDimension,CheckBoxDimension);
        Quad = new TextureQuad(RenderUtils,GetCurrentTextureID(),CheckBoxRect, TexCoord.SimpleQuad);

        if( !Text.isEmpty() ) {
            TextSize = FontSize.FONT_MEDIUM;
            TextLabel = new GLString(RenderUtils, TextSize,Text,Rectangle,TextColor,
                    GLTextAlign.ALIGN_LEFT);
        }
        Active = true;
    }

    public GLCheckBox(RenderEngine RenderUtils, int ResourceID, String Text,boolean Checked,
                      GLWidgetCheckBoxChangeCallback Callback) {
        super(RenderUtils,new Rect(),ResourceID);
        this.Text = Text;
        this.TextColor = Color4.Black;
        this.Checked = Checked;
        this.Callback = Callback;
        Init();
    }

    public GLCheckBox(RenderEngine RenderUtils, int ResourceID, String Text,GLWidgetCheckBoxChangeCallback Callback) {
        super(RenderUtils,new Rect(),ResourceID);
        this.Text = Text;
        this.TextColor = Color4.Black;
        this.Callback = Callback;
        this.Checked = false;
        Init();
    }
}
