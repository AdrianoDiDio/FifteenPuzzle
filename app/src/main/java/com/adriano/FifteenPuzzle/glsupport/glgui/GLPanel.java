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

import android.opengl.GLES30;

import com.adriano.FifteenPuzzle.glsupport.ColorQuad;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.UUID;

import timber.log.Timber;

/*
* TODO:
* GLPanel: Add Alignment (ALIGN_VERTICAL,ALIGN_HORIZONTAL etc...)
* GLPanel: Set Background texture/color.
* */
public class GLPanel extends GLWidget {
    protected ColorQuad Quad;
    protected Color4 Color;
    protected LinkedList/*ArrayList*/<GLWidget> WidgetList;

    public GLWidget GetWidgetByID(UUID ID) {
        for( GLWidget Widget : WidgetList ) {
            if( Widget.ID.compareTo(ID) == 0 ) {
                return Widget;
            }
        }
        return null;
    }
    public void SetColor(Color4 Color) {
        this.Color = Color;
        Quad.SetColor(Color);
        Quad.UpdateData();
    }

    @Override
    public void SetRectangle(Rect Rectangle) {
        super.SetRectangle(Rectangle);
        Quad.SetRectangle(Rectangle);
        Quad.UpdateData();
    }

    @Override
    public boolean OnTouch(Vec2 Position) {
        if( !IsActive() ) {
            return false;
        }
        if( Rectangle.Inside(Position) ) {
            for( GLWidget Widget : WidgetList) {
                Widget.OnTouch(Position);
            }
            if( Callback != null ) {
                Callback.OnClick(new GLClickEvent(ID,null));
            }
        }
        return true;
    }

    public void Add(GLWidget Widget) {
        Widget.ParentID = ID;
        WidgetList.add(Widget);
    }

    @Override
    public void Draw() {
        if( !IsActive() ) {
            return;
        }
        if( Quad == null ) {
            Timber.e("NULL QUAD");
            return;
        }
        GLES30.glEnable(GLES30.GL_BLEND);
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA);
        Quad.Draw();
        for( GLWidget Widget : WidgetList) {
            Widget.Draw();
        }
        GLES30.glDisable(GLES30.GL_BLEND);
    }

    private void Init() {
        Quad = new ColorQuad(RenderUtils, Rectangle,Color);
        WidgetList = new LinkedList<>();
    }

    public GLPanel(RenderEngine RenderUtils,Rect Rectangle,int ResourceID) {
        super(RenderUtils,Rectangle,ResourceID);
        this.Color = Color4.Black;
        Init();
    }

    public GLPanel(RenderEngine RenderUtils, Rect Rectangle, Color4 Color,int ResourceID) {
        super(RenderUtils,Rectangle,ResourceID);
        this.Color = Color;
        Init();
    }

    public GLPanel(RenderEngine RenderUtils,Color4 Color,int ResourceID) {
        super(RenderUtils,new Rect(Vec2.ZeroVector(),0,0),ResourceID);
        this.Rectangle = new Rect(Vec2.ZeroVector(),0,0);
        this.Color = Color;
        Init();
    }
}
