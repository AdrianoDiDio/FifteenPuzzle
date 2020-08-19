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

import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

public class GLLabel extends GLWidget {
    private String Text;
    private GLString TextLabel;
    private FontSize TextSize;

    @Override
    public boolean OnTouch(Vec2 Position) {
        return false;
    }

    @Override
    public void SetRectangle(Rect Rectangle) {
        super.SetRectangle(Rectangle);
        TextLabel.SetContainer(Rectangle);
    }

    @Override
    public void Draw() {
        if( !Active ) {
            return;
        }
        TextLabel.Draw();
    }

    private void Init() {
        TextLabel = new GLString(RenderUtils,TextSize,Text,Rectangle, Color4.White,GLTextAlign.ALIGN_CENTER);
    }

    public GLLabel(RenderEngine RenderUtils, int ResourceID, String Text, FontSize TextSize) {
        super(RenderUtils,new Rect(),ResourceID);
        this.Text = Text;
        this.TextSize = TextSize;
        Init();
    }

    public GLLabel(RenderEngine RenderUtils, int ResourceID,String Text,Rect Rectangle) {
        super(RenderUtils,Rectangle,ResourceID);
        this.Text = Text;
        this.TextSize = FontSize.FONT_MEDIUM;
        Init();
    }
}
