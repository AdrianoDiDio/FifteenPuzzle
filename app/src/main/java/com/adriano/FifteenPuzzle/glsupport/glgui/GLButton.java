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

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.TextureQuad;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.TexCoord;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class GLButton extends GLWidget {
    private TextureQuad Quad;
    private String Text;
    private GLString GLText;
    private Color4 TextColor;
    private FontSize TextSize;
    private int CustomTextureID;


    private boolean HasText() {
        return GLText != null && !Text.isEmpty();
    }
    /*
        If we have a valid text that we need to put over the button
        check if the width of the given rect for this button fits
        the string width and resize the button accordingly.
        Returns the argument rectangle if text is empty otherwise
        a new rect that will for sure contains the given String.
     */
    private Rect CheckAndFitRectWidth(Rect Rectangle) {
        Rect Temp;
        float StringWidth;

        Temp = new Rect(Rectangle);

        if( HasText() ) {
            StringWidth = RenderUtils.FontSystem.GetStringWidth(Text,TextSize);
            //If it doesn't fit make sure we have enough space.
            if( Rectangle.Width < StringWidth ) {
                Temp.Width = StringWidth;
            }
        }
        return Temp;
    }
    @Override
    public boolean OnTouch(Vec2 Position) {
        if( Rectangle.Inside(Position) ) {
            RenderUtils.SoundUtils.Play("ButtonClick");
            Callback.OnClick(new GLClickEvent(ID,ParentID));
            return true;
        }
        return false;
    }


    @Override
    public void SetRectangle(Rect Rectangle) {
        Rect Temp;
        Temp = CheckAndFitRectWidth(Rectangle);
        super.SetRectangle(Temp);
        Quad.SetRectangle(Temp);
        if( HasText() ) {
            GLText.SetContainer(Temp);
        }
        Quad.UpdateData();
    }

    @Override
    public void Draw() {
        if( !Active ) {
            return;
        }
        Quad.Draw();
        if( HasText() ) {
            GLText.Draw();
        }
    }

    private void Init() {
        Rect FinalRect;
        int TextureID;
        FinalRect = CheckAndFitRectWidth(Rectangle);
        //Make sure to update it...
        if( FinalRect.Width != Rectangle.Width ) {
            this.Rectangle = new Rect(FinalRect);
        }
        if( CustomTextureID == -1 ) {
            TextureID = RenderUtils.TextureUtils.Cache("BlueButton", R.raw.bluebutton);
        } else {
            TextureID = CustomTextureID;
        }
        Quad = new TextureQuad(RenderUtils,TextureID,Rectangle, TexCoord.SimpleQuad);

        if( !Text.isEmpty() ) {
            TextSize = FontSize.FONT_MEDIUM;
            GLText = new GLString(RenderUtils, TextSize,Text,Rectangle,TextColor,
                    GLTextAlign.ALIGN_CENTER);

        }
        RenderUtils.SoundUtils.Cache("ButtonClick", R.raw.click1);
        Active = true;
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID, Rect Rectangle, String Text, GLWidgetTouchCallback Callback) {
        super(RenderUtils,Rectangle,ResourceID);
        this.Text = Text;
        this.TextColor = Color4.White;
        this.Callback = Callback;
        this.CustomTextureID = -1;
        Init();
        //Quad = new TextureQuad()
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID, Rect Rectangle, int CustomTextureID, GLWidgetTouchCallback Callback) {
        super(RenderUtils,Rectangle,ResourceID);
        this.Text = "";
        this.TextColor = Color4.White;
        this.Callback = Callback;
        this.CustomTextureID = CustomTextureID;
        Init();
        //Quad = new TextureQuad()
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID, Rect Rectangle, String Text, Color4 TextColor, GLWidgetTouchCallback Callback) {
        super(RenderUtils,Rectangle,ResourceID,Callback);
        this.Text = Text;
        this.TextColor = TextColor;
        this.Callback = Callback;
        this.CustomTextureID = -1;
        Init();
        //Quad = new TextureQuad()
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID, Rect Rectangle, GLWidgetTouchCallback Callback) {
        super(RenderUtils,Rectangle,ResourceID,Callback);
        this.Text = "";
        this.Callback = Callback;
        this.CustomTextureID = -1;
        Init();
        //Quad = new TextureQuad()
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID,int CustomTextureID,GLWidgetTouchCallback Callback) {
        super(RenderUtils,ResourceID,Callback);
        this.Text = "";
        this.TextColor = Color4.White;
        this.Callback = Callback;
        this.CustomTextureID = CustomTextureID;
        Init();
        //Quad = new TextureQuad()
    }

    public GLButton(RenderEngine RenderUtils, int ResourceID,String Text,GLWidgetTouchCallback Callback) {
        super(RenderUtils,ResourceID,Callback);
        this.Text = Text;
        this.TextColor = Color4.White;
        this.Callback = Callback;
        this.CustomTextureID = -1;
        Init();
        //Quad = new TextureQuad()
    }
}
