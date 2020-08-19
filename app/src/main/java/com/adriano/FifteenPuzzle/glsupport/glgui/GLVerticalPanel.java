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


import android.util.TimingLogger;

import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import timber.log.Timber;

/*
* Simple panel that adds his child widget at the center of
* the panel in a vertical column.
* */
public class GLVerticalPanel extends GLPanel{


    public void UpdateLayout() {
        Rect WidgetRect;
        float CenterX;
        float FixedWidth;
        float IdealHeight;
        float AvailableHeight;
        float Spacing;

        float AdvanceY; // Added widget will use this as position offset.

        FixedWidth = Rectangle.Width - 50.f;
        Spacing = 20.f;
        AvailableHeight = Rectangle.Height - (Spacing * WidgetList.size());
        IdealHeight = AvailableHeight / WidgetList.size();
        AdvanceY = 0.f;

        CenterX = (Rectangle.Width - FixedWidth) / 2;
        for( GLWidget Widget : WidgetList) {
            WidgetRect = new Rect(new Vec2(CenterX, AdvanceY), FixedWidth, IdealHeight);
            WidgetRect.Position.Add(Rectangle.Position);
            Widget.SetRectangle(WidgetRect);
            AdvanceY += IdealHeight + Spacing;
        }
    }
    @Override
    public void Add(GLWidget Widget) {
        super.Add(Widget);
        UpdateLayout();
    }


    public GLVerticalPanel(RenderEngine RenderUtils,Rect Rectangle,Color4 Color, int ResourceID) {
        super(RenderUtils,Rectangle,Color,ResourceID);
    }

    public GLVerticalPanel(RenderEngine RenderUtils, Color4 Color, int ResourceID) {
        super(RenderUtils,Color,ResourceID);
    }
}
