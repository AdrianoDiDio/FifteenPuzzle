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
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import timber.log.Timber;

/*
 * Simple panel that adds his child widget at the center of
 * the panel in a horizontal row.
 * */
public class GLHorizontalPanel extends GLPanel{


    public void UpdateLayout() {
        Rect WidgetRect;
        float CenterY;
        float FixedHeight;
        float IdealWidth;
        float AvailableWidth;
        float Spacing;

        float AdvanceX; // Added widget will use this as position offset.

        FixedHeight = Rectangle.Height - 25.f;
        Spacing = 30.f;
        AvailableWidth = Rectangle.Width - (Spacing * WidgetList.size());
        IdealWidth = AvailableWidth / WidgetList.size();
        AdvanceX = 0.f;

        CenterY = (Rectangle.Height - FixedHeight) / 2;
        for( GLWidget Widget : WidgetList) {
            WidgetRect = new Rect(new Vec2(AdvanceX, CenterY), IdealWidth, FixedHeight);
            WidgetRect.Position.Add(Rectangle.Position);
            Widget.SetRectangle(WidgetRect);
            AdvanceX += IdealWidth + Spacing;
        }
    }
    @Override
    public void Add(GLWidget Widget) {
        super.Add(Widget);
        UpdateLayout();
    }


    public GLHorizontalPanel(RenderEngine RenderUtils,Rect Rectangle,Color4 Color, int ResourceID) {
        super(RenderUtils,Rectangle,Color,ResourceID);
    }

    public GLHorizontalPanel(RenderEngine RenderUtils, Color4 Color, int ResourceID) {
        super(RenderUtils,Color,ResourceID);
    }
}

