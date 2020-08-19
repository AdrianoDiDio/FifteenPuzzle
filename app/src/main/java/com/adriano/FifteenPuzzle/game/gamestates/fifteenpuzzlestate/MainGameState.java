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
package com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate;

import android.content.pm.ActivityInfo;
import android.view.MotionEvent;

import com.adriano.FifteenPuzzle.game.GameState;
import com.adriano.FifteenPuzzle.game.GameStateManager;
import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.game.gamestates.GameOverState;
import com.adriano.FifteenPuzzle.game.gamestates.MainMenuState;
import com.adriano.FifteenPuzzle.game.gamestates.gameutils.CountdownTimer;
import com.adriano.FifteenPuzzle.game.gamestates.gameutils.TimeOutCallback;
import com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.gridutils.GridTile;
import com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.gridutils.HoleTile;
import com.adriano.FifteenPuzzle.game.gamestates.fifteenpuzzlestate.gridutils.TextureTile;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLButton;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLClickEvent;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLHorizontalPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLPanel;
import com.adriano.FifteenPuzzle.glsupport.glgui.GLWidgetTouchCallback;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.glsupport.renderengine.SurfaceChangedObserver;
import com.adriano.FifteenPuzzle.glsupport.renderengine.font.FontSize;
import com.adriano.FifteenPuzzle.utils.Color4;
import com.adriano.FifteenPuzzle.utils.Rect;
import com.adriano.FifteenPuzzle.utils.Vec2;

import java.util.Random;

import timber.log.Timber;

/*
* TODO: Fix tile position when rotating phone.
* TODO: Add an IsActive to Pause/Restore game and also to wait till shuffle is over.
* TODO: Complete tile movement code.
* TODO: Make sure that when we move a tile we move the ?texturetile? and not the hole.
*       Actually implement Movement code in the hole tile to update his own position in grid.
* */

public class MainGameState extends GameState implements SurfaceChangedObserver, TimeOutCallback,
        GLWidgetTouchCallback {
    //private ColorQuad[] ScreenZone;
    private GLPanel GameHeaderPanel;
    private GLPanel MainGamePanel;
    //private ArrayList<GridTile> Grid;
    private GridTile[] Grid;
    private int GridSize;
    private CountdownTimer GameTimer;
    //TODO:Move to PlayerPrefs.
    private boolean SmoothTileMotion;
    private boolean Shuffling;

    private GLHorizontalPanel ButtonsPanel;
    private GLButton BackButton;
    private GLButton RefreshButton;


    @Override
    public void OnClick(GLClickEvent Event) {
        //We only have one for the moment...
        GLButton Button;
        Button = (GLButton) ButtonsPanel.GetWidgetByID(Event.SourceID);
        if( Button.GetResourceID() == R.id.BackButton ) {
            ChangeState(new MainMenuState(Manager));
            return;
        }
        Timber.d("Unknown button Resource ID " + Button.GetResourceID());
    }

    @Override
    public void OnTimeOut() {
        Timber.d("Time is UP!");
        ChangeState(new GameOverState(Manager,false,GridSize));
        //ChangeState(new GameOverState(Manager));
    }

    @Override
    public void SurfaceChanged() {
        Timber.d("Surface has changed");
        InitZone();
        UpdateGrid();

        //GameHeaderPanel.OnSurfaceChanged();
        //MainGamePanel.OnSurfaceChanged();


        //UpdateGrid();

    }
    private void CheckWinCondition() {
        boolean HasWin;
        HasWin = true;
        for( int i = 0; i < Grid.length; i++ ) {
            int ButtonPosition = i+1;
            if( Grid[i] instanceof HoleTile ) {
                continue;
            }
            if( ButtonPosition != Grid[i].GetNumber() ) {
                Timber.d("I != Number: " + i + " " + Grid[i].GetNumber());
                HasWin = false;
                break;
            }
        }
        if( HasWin ) {
            Timber.d("Player won...");
            ChangeState(new GameOverState(Manager,true,GridSize));
        } else {
            Timber.d("Tiles not in order...");
        }
    }

    private void CheckAndSwapTile(GridTile Tile,int TileIndex) {
        Vec2 TilePositionInGrid;
        Vec2 NeighbourPositionInGrid;

        TilePositionInGrid = TileIndexToTilePosition(TileIndex);

        for( int XOffset = -1; XOffset < 2; XOffset += 2 ) {
            int NeighbourIndex = GetTileIndexFromGridPosition(
                    new Vec2(TilePositionInGrid.x + XOffset,TilePositionInGrid.y));
            Vec2 NeighbourPos = TileIndexToTilePosition(NeighbourIndex);

            //Off bounds.
            if( NeighbourIndex < 0 || NeighbourIndex >= Grid.length ) {
                Timber.d("Neighbour is out of bounds " + NeighbourIndex);
                continue;
            }
            Timber.d("Neighbour is " + Grid[NeighbourIndex].GetNumber());
            //We are allowed to move only vertically!
            if( Grid[NeighbourIndex] instanceof HoleTile && NeighbourPos.y == TilePositionInGrid.y) {
                //Thats it...swap me!
                Timber.d("Swapping " + TileIndex + " with " + NeighbourIndex);
                Timber.d("Numbers is " + Tile.GetNumber() + " with " +
                        Grid[NeighbourIndex].GetNumber());
                SwapTile(Tile.GetNumber(), Grid[NeighbourIndex].GetNumber());
                CheckWinCondition();
                return;
            }
        }

        for( int YOffset = -1; YOffset < 2; YOffset += 2 ) {
            int NeighbourIndex = GetTileIndexFromGridPosition(
                    new Vec2(TilePositionInGrid.x,TilePositionInGrid.y + YOffset));
            Vec2 NeighbourPos = TileIndexToTilePosition(NeighbourIndex);

            //Off bounds.
            if( NeighbourIndex < 0 || NeighbourIndex >= Grid.length ) {
                Timber.d("Neighbour is out of bounds.");
                continue;
            }
            Timber.d("Neighbour is " + Grid[NeighbourIndex].GetNumber());
            Timber.d("Neighbour is " + Grid[NeighbourIndex].GetNumber());
            //We are allowed to move only horizontally!
            if( Grid[NeighbourIndex] instanceof HoleTile && NeighbourPos.x == TilePositionInGrid.x) {
                //Thats it...swap me!
                Timber.d("Swapping " + TileIndex + " with " + NeighbourIndex);
                Timber.d("Numbers is " + Tile.GetNumber() + " with " +
                        Grid[NeighbourIndex].GetNumber());
                SwapTile(Tile.GetNumber(), Grid[NeighbourIndex].GetNumber());
                //SwapButtons(ButtonIndex,NeighbourIndex);
                CheckWinCondition();
                return;
            }
        }
    }

    private void TestSwap() {
        Vec2 TempPos;
        int AIndex;
        int BIndex;
        AIndex = GetTileIndexByTileNumber(8);
        BIndex = GetTileIndexByTileNumber(9);
        TempPos = Grid[AIndex].GetPosition();
        Grid[AIndex].Move(Grid[AIndex].GetRect().Position,Grid[BIndex].GetRect().Position);
        Grid[BIndex].Move(Grid[BIndex].GetRect().Position,TempPos);
    }
    @Override
    public boolean OnTouch(MotionEvent Event) {
        if( !Initialized ) {
            return false;
        }
        ButtonsPanel.OnTouch(new Vec2(Event.getX(),Event.getY()));
        for( int i = 0; i < Grid.length; i++ ) {
            if( Grid[i] instanceof HoleTile ) {
                continue;
            }
            if( Grid[i].GetRect().Inside(Event.getX(),Event.getY()) ) {
                Timber.d("Hit tile " + Grid[i].GetNumber());
                /*RenderUtils.QueueEvent(new Runnable() {
                    @Override
                    public void run() {
                        TestSwap();
                    }
                });*/
                CheckAndSwapTile(Grid[i],i);
                break;
            }
        }
       /* int A;
        int B;

        A = GetTileIndexByTileNumber(1);
        B = GetTileIndexByTileNumber(2);
        Timber.d("Swapping " + A + " with " + B);
        SwapTile(A,B);*/

        return false;
    }

    @Override
    public void OnDestroy() {
        GameTimer.Stop();
    }

    @Override
    public void Pause() {
    }

    @Override
    public void Draw() {
        if( !Initialized ) {
            return;
        }

        /*if( ScreenZone == null ) {
            return;
        }
        for( int i = 0; i < ScreenZone.length; i++ ) {
            ScreenZone[i].Draw();
        }*/
        ButtonsPanel.Draw();
        GameHeaderPanel.Draw();
        MainGamePanel.Draw();
        GameTimer.Draw();

        for( GridTile Tile : Grid) {
            Tile.Draw();
        }
        /*
        for( ThickLine l : GridOutline ) {
            //l.Draw();
        }
        for( ColorQuad Q : Grid ) {
            Q.Draw();
        }*/

        /*for( int x = 0; x < 16; x++ ) {
                Grid[x].Draw();
        }*/
    }

    int GetTileIndexByTileNumber(int Number) {
        for(int i = 0; i < Grid.length; i++ ) {
            if( Grid[i].GetNumber() == Number ) {
                return i;
            }
        }
        return -1;
    }

    private int GetTileIndexFromGridPosition(Vec2 Position) {
        return (int) ((GridSize * Position.x) + Position.y);
    }

    boolean Cached(int Number) {
        for(int i = 0; i < Grid.length; i++ ) {
            if( Grid[i] != null && Grid[i].GetNumber() == Number ) {
                return true;
            }
        }
        return false;
    }

    int GetHoleIndex() {
        for(int i = 0; i < Grid.length; i++ ) {
            if( Grid[i] instanceof HoleTile ) {
                return i;
            }
        }
        return -1;
    }

    /*
    If Number is odd then ANDing with 1
    result in a number which has the right most bit set to 1.
 */
    private boolean IsOdd(int Number) {
        return (Number & 1) == 1;
    }

    /*
    Returns a vector containing the coordinates of the cell based on the array position.
    X => Row;
    Y => Column;
    */
    private Vec2 TileIndexToTilePosition(int Index) {
        return new Vec2(Index/GridSize,Index % GridSize);
    }

    private void DumpGrid() {
        Timber.d("**DUMPING GRID**");
        Timber.d("Grid size is " + Grid.length);
        Timber.d("Grid Size should be " + GridSize + "X" + GridSize);
        for(int i = 0; i < Grid.length; i++ ) {
            Timber.d("Tile Rectangle " + i + " has number " + Grid[i].GetNumber());
            if( Grid[i].GetNumber() == (GridSize * GridSize) ) {
                if( Grid[i] instanceof HoleTile ) {
                    Timber.d("...and it is a hole tile.");
                }
            }
        }
    }
    private void SwapTile(int GridTileA,int GridTileB) {
        GridTile A;
        GridTile B;
        GridTile Temp;
        Vec2 TempPos;
        int AIndex;
        int BIndex;
        int     TempNumber;

        AIndex = GetTileIndexByTileNumber(GridTileA);
        BIndex = GetTileIndexByTileNumber(GridTileB);

        if( AIndex == BIndex ) {
            return;
        }

        A = Grid[AIndex];
        B = Grid[BIndex];

        if( A instanceof HoleTile || B instanceof HoleTile ) {
            if(SmoothTileMotion && !Shuffling ) {
              TempPos = Grid[AIndex].GetPosition();
              Grid[AIndex].Move(Grid[AIndex].GetRect().Position, Grid[BIndex].GetRect().Position);
              Grid[BIndex].Move(Grid[BIndex].GetRect().Position, TempPos);
            } else {
                TempPos = A.GetPosition();
                A.SetPosition(B.GetPosition());
                B.SetPosition(TempPos);
            }
            Temp = Grid[AIndex];
            Grid[AIndex] = Grid[BIndex];
            Grid[BIndex] = Temp;
            return;
        }

        TempNumber = A.GetNumber();
        A.SetNumber(B.GetNumber());
        B.SetNumber(TempNumber);
    }


    private void Shuffle() {
        Random RandomGenerator;
        int RandomIndex;
        int TileIndex;

        RandomGenerator = new Random();
        Timber.d("Shuffle grid size is " + Grid.length);
        for(int i = 0; i < Grid.length; i++ ) {
            //Generate a random index between 1-Grid.length...
            RandomIndex = RandomGenerator.nextInt((Grid.length - 2) + 1) + 1;
            SwapTile(Grid[i].GetNumber(),RandomIndex);
        }
    }

    private int GetNumberOfInversions() {
        int NumInversions = 0;
        for(int i = 0; i < Grid.length; i++ ) {
            for(int j = i + 1; j < Grid.length; j++ ) {
                if( Grid[i] instanceof HoleTile || Grid[j] instanceof HoleTile ) {
                    continue;
                }
                if( Grid[i].GetNumber() > Grid[j].GetNumber() ) {
                    NumInversions++;
                }
            }
        }
        return NumInversions;
    }

    /*
        Adriano:
        Given that our grid is 4x4 to see if the game can be solved
        we need to check 2 conditions depending on the hole position:
            - If the hole entity is on an even row counting from bottom
              and number of inversions is odd then can be solved.
            - If the hole is on an odd row counting from the bottom and number
              of inversions is even.
        Note that this is not true if the grid was not even (5x5,3x3 etc).
        In that case we just need to count the number of inversions and see if It is even.
     */
    private boolean IsSolvable() {
        int HoleIndex;
        int HoleRow;
        int RowNumberFromLast;
        int NumInversions;

        //First thing first get Hole position.
        HoleIndex = GetHoleIndex();
        // Get the row of the button!
        HoleRow = (int) TileIndexToTilePosition(HoleIndex).x;
        //Counting from the bottom check if the row count is even or odd!
        RowNumberFromLast = GridSize - HoleRow;
        NumInversions = GetNumberOfInversions();
        Timber.d("Hole Row is: " + HoleRow);
        Timber.d("Hole Column is: " + TileIndexToTilePosition(HoleIndex).y);
        Timber.d("RowNumberFromLast is: " + RowNumberFromLast);
        Timber.d("Inversion Count is: " + NumInversions);

        // If the grid size is odd (3x3) then we just need to find out if the number of inversions
        // is even.
        if( IsOdd(GridSize) ) {
            if( !IsOdd(NumInversions) ) {
                return true;
            }
        } else {
            if( IsOdd(RowNumberFromLast) ) {
                if( !IsOdd(NumInversions) ) {
                    return true;
                }
            } else {
                //Even
                if( IsOdd(NumInversions) ) {
                    return true;
                }
            }
        }
        return false;
    }

    private void Refresh() {
        int DebugNumShuffle = 0;
        Shuffling = true;
        do {
            DebugNumShuffle++;
            Shuffle();
        } while( !IsSolvable() );
        Shuffling = false;
        Timber.d("Took " + DebugNumShuffle + " shuffles to create a valid game.");
        GameTimer.Reset();
        GameTimer.Start();
    }

    /*
    * */
    private void UpdateGrid() {
        Vec2 GridOrigin;
        float TileWidth;
        float TileHeight;
        float TileStep;
        float NumTiles;
        float CenteredX;
        float SumX;
        float BreakX;
        Vec2 TilePosition;
        Rect GridTileRect;
        int TileNumber;

        if( Grid == null ) {
            Grid = new GridTile[GridSize * GridSize];
        }

        NumTiles = (GridSize * GridSize);

        Rect GridZone;

        GridZone = new Rect(Vec2.ZeroVector(),RenderUtils.Width,RenderUtils.Height -
                (0.10f * RenderUtils.Height));

        TileWidth = (float) Math.ceil(GridZone.Width / GridSize);
        TileHeight = (float) Math.ceil(GridZone.Height / GridSize);

        TileStep = Math.min(TileWidth,TileHeight);


        CenteredX = RenderUtils.Width / 2.f;
        CenteredX -= (GridSize * TileStep) / 2.f;

        GridZone.Position = new Vec2(CenteredX,MainGamePanel.GetRectangle().Position.y);

        GridOrigin = new Vec2(GridZone.Position);
        Timber.d("UPDATE GRID");
        Timber.d("Render Resolution is " + RenderUtils.Width + "X" + RenderUtils.Height);
        Timber.d("GridOrigin is " + GridOrigin.toString());
        Timber.d("TileStep is: " + TileStep);
        TilePosition = new Vec2(GridOrigin);

        for (int i = 0; i < NumTiles; i++) {
            TileNumber = i+1;
            SumX = TilePosition.x + TileStep;

            BreakX = GridOrigin.x + (TileStep * GridSize);
            Timber.d("Working with tile " + i + " Number " + TileNumber);
            Timber.d("Grid Rectangle is: " + GridOrigin.toString());
            Timber.d("Tile Rectangle is: " + TilePosition.toString());
            Timber.d("Breaking line");
            Timber.d("SumX: " + SumX);
            Timber.d("BreakX: " + BreakX);
            if( SumX > BreakX ) {
                TilePosition.y += TileStep;
                TilePosition.x  = GridOrigin.x;
            }
            GridTileRect = new Rect(TilePosition, TileStep, TileStep);

            if( !Cached( TileNumber ) ) {
                Timber.w("Warning tile number " + TileNumber + " was not found in list.");
                Timber.d("Caching it...");
                if( TileNumber == NumTiles ) {
                    Timber.d("Added a new hole..." + TileNumber);
                    Grid[i] = new HoleTile(RenderUtils,TileNumber,GridTileRect);
                } else {
                    Timber.d("Added a new texture tile..." + TileNumber);
                    Grid[i] = new TextureTile(RenderUtils,TileNumber,GridTileRect);
                }
            } else {
                Timber.d("Updating position");
                Grid[i].SetRect(GridTileRect);
            }
            TilePosition.x += TileStep;
        }
    }

    private void InitZone() {
        float ButtonWidth;
        float HeaderHeight;
        float BoardHeight;

        HeaderHeight = (7.f * RenderUtils.Height) / 100.f;
        BoardHeight = (92.f * RenderUtils.Height) / 100.f;

        ButtonWidth = (7.f * RenderUtils.Width) / 100.f;

        Timber.d("Total Width: " + RenderUtils.Width);
        Timber.d("Total Height: " + RenderUtils.Height);
        Timber.d("Header Height: " + HeaderHeight);
        Timber.d("Board Height: " + BoardHeight);

        GameHeaderPanel.SetRectangle(new Rect(Vec2.ZeroVector(),RenderUtils.Width,HeaderHeight));
        MainGamePanel.SetRectangle(new Rect(new Vec2(0.f,HeaderHeight),RenderUtils.Width,RenderUtils.Height));
        GameTimer.CenterText(GameHeaderPanel.GetRectangle());
        ButtonsPanel.SetRectangle(new Rect(Vec2.ZeroVector(),ButtonWidth,HeaderHeight));
        ButtonsPanel.UpdateLayout();
    }


    private void InitButtonsPanel() {
        int BackButtonTextureID;
        ButtonsPanel = new GLHorizontalPanel(RenderUtils,
                new Rect(),
                Color4.TransparentWhite, R.id.ButtonsPanel);
        //Cache it
        //R.raw.backbutton
        BackButtonTextureID = RenderUtils.TextureUtils.Cache("BlueBackButton", R.raw.bluebackbutton);
        ButtonsPanel.Add(new GLButton(RenderUtils,R.id.BackButton,BackButtonTextureID,this));

    }


    @Override
    public void Init(RenderEngine RenderUtils) {
        Color4 BGColor;
        super.Init(RenderUtils, ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE,false);
        RenderUtils.RegisterSurfaceChangedObserver(this);
        if( GridSize < 3 || GridSize > 4 ) {
            GridSize = 3;
        }
        SmoothTileMotion = RenderUtils.Settings.Preferences.getBoolean("SmoothTileMotion",
                false);
        GameTimer = new CountdownTimer(RenderUtils,new Vec2(800.f,0.f),120,
                FontSize.FONT_MEDIUM,this);
        BGColor = Color4.LightBlue;
        GameHeaderPanel = new GLPanel(RenderUtils,BGColor,R.id.GameHeaderPanel);


        InitButtonsPanel();

        MainGamePanel = new GLPanel(RenderUtils,BGColor,R.id.MainGamePanel);
        InitZone();
        UpdateGrid();
        Refresh();
        Initialized = true;
    }

    public MainGameState(GameStateManager Manager,int GridSize) {
        super(Manager);
        this.GridSize = GridSize;
    }
}
