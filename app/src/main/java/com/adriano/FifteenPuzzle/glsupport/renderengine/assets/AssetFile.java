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
package com.adriano.FifteenPuzzle.glsupport.renderengine.assets;

import android.support.annotation.Nullable;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

public class AssetFile {
    private String Name;
    private byte[] Data;

    public String GetName() {
        return Name;
    }

    @Override
    public int hashCode() {
        return Name.hashCode();
    }

    @Override
    public boolean equals(@Nullable Object Obj) {
        AssetFile Other;
        if( this == Obj ) {
            return true;
        }
        if( !(Obj instanceof AssetFile ) ) {
            return false;
        }
        Other = (AssetFile) Obj;

        if( Name.equals(Other.Name) ) {
            return true;
        }
        return false;
    }

    public InputStream GetInputStream() {
        return new ByteArrayInputStream(Data);
    }

    private byte[] ToByteArray(InputStream InStream) {
        try {
            ByteArrayOutputStream OutStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int readLength = 0;
            while ((readLength = InStream.read(buffer)) != -1) {
                OutStream.write(buffer, 0, readLength);
            }
            OutStream.flush();
            return OutStream.toByteArray();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    private void InitFromBuffer(byte[] Buffer) {
        this.Data = new byte[Buffer.length];
        System.arraycopy(Buffer,0,Data,0,Buffer.length);
    }


    public AssetFile(String Name,byte[] Buffer) {
        this.Name = Name;
        InitFromBuffer(Buffer);
        //this.Data = Buffer;
    }
    public AssetFile(String Name,InputStream Stream) {
        this.Name = Name;
        this.Data = ToByteArray(Stream);
    }

    public AssetFile(AssetFile Other) {
        this.Name = Other.Name;
        InitFromBuffer(Other.Data);
    }
}
