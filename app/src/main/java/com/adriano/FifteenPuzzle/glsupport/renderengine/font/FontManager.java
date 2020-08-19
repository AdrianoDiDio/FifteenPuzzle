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
package com.adriano.FifteenPuzzle.glsupport.renderengine.font;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.renderengine.assets.AssetFile;
import com.adriano.FifteenPuzzle.glsupport.renderengine.assets.AssetUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import timber.log.Timber;

/*
* FONT FILE TYPE
* typedef struct Char_s
{
  Byte   c;
  short  x;
  short  y;
  short  Width;
  short  Height;
  //Version 3
  short  AdvanceX;
  short  BearingX;
}Char_t;

typedef struct FontHeader_s
{
  Byte  Magic[5];
  Byte  Version;
  short  Width;
  short  Height;
  Byte  CWidth;
  Byte  CHeight;
  Byte  FSize;
  Char_t Chars[256];
}FontHeader_t;
*
* */
public class FontManager {
    private Context AndroidContext;
    private AssetUtils Assets;
    public FontInfo[] Font;

    public int GetStringWidth(String Text, FontSize TextSize) {
        return Font[TextSize.Value].GetStringWidth(Text);
    }

    public int GetStringHeight(String Text, FontSize TextSize) {
        return Font[TextSize.Value].GetStringHeight(Text);
    }

    private void LoadFromResourceID(FontSize Size,
                                    AssetFile FontDataFile,AssetFile FontImageFile) {
        InputStream InStream;
        ByteBuffer TempBuffer;
        ReadableByteChannel FChannel;
        byte[] Magic;
        int Version;
        int i;

        Magic = new byte[5];

        Font[Size.Value] = new FontInfo();
        Font[Size.Value].Name = Size.name();
        try {
            InStream = FontDataFile.GetInputStream();
            FChannel = Channels.newChannel(InStream);
            //Really big buffer..
            TempBuffer = ByteBuffer.allocate(InStream.available());
            TempBuffer.order(ByteOrder.LITTLE_ENDIAN);
            FChannel.read(TempBuffer);
            TempBuffer.flip();
            TempBuffer.get(Magic);
            Version = TempBuffer.get();

            Timber.d("Magic: " + new String(Magic, "UTF-8"));
            Timber.d("Version: " + Version);

            Font[Size.Value].Width = TempBuffer.getShort();
            Font[Size.Value].Height = TempBuffer.getShort();
            Font[Size.Value].CWidth = TempBuffer.get();
            Font[Size.Value].CHeight = TempBuffer.get();
            Font[Size.Value].LineHeight = TempBuffer.getShort();
            Font[Size.Value].FSize = TempBuffer.get();
            Timber.d(Font[Size.Value].toString());
            for (i = 0; i < 256; i++) {
                Font[Size.Value].Character[i] = new CharInfo();
                Font[Size.Value].Character[i].c = (char) TempBuffer.get();
                Font[Size.Value].Character[i].x = TempBuffer.getShort();
                Font[Size.Value].Character[i].y = TempBuffer.getShort();
                Font[Size.Value].Character[i].Width = TempBuffer.getShort();
                Font[Size.Value].Character[i].Height = TempBuffer.getShort();
                Font[Size.Value].Character[i].AdvanceX = TempBuffer.getShort();
                Font[Size.Value].Character[i].BearingX = TempBuffer.getShort();
            }
            Timber.d("Loading Image.");
            Font[Size.Value].Image = BitmapFactory.decodeStream(FontImageFile.GetInputStream());
        } catch (IOException e) {
            Timber.e("IoException: " + e.getMessage());
        }

    }

    private void Load() {
        String[] FontFiles;
        AssetFile FontData;
        AssetFile FontImage;
        FontSize FSize;

        FontFiles = AndroidContext.getResources().getStringArray(R.array.FontFiles);
        for( String Font : FontFiles ) {
            FSize = FontSize.Get(Font);
            FontData = Assets.Get(Font + ".bin");
            FontImage = Assets.Get(Font + ".png");
            Timber.d("Loading size " + FSize + " based on " + Font);
            LoadFromResourceID(FSize,FontData,FontImage);
        }
    }

    /*
     * Context is required in order to be able to load the resources...
     *
     * */
    public FontManager(Context AndroidContext, AssetUtils Assets) {
        Font = new FontInfo[3];
        this.AndroidContext = AndroidContext;
        this.Assets = Assets;
        Load();
    }
}
