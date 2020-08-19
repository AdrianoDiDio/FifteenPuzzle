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

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.text.TextUtils;

import com.adriano.FifteenPuzzle.R;
import com.adriano.FifteenPuzzle.glsupport.renderengine.RenderEngine;
import com.adriano.FifteenPuzzle.utils.ContextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import timber.log.Timber;

/*
* When opening a zip file cache the content into a list ready to be searched/used
* So at the beginning of the app lifecycle we cache everything and then request the
* file using standard ID like "CheckBox.png" "CheckBoxOn.png" etc...
* In this way we can selectively choose what we need also we can set a timestamp on resources
* to remove unused one after a certain amount of time...
* Bear in mind that this could cause an error when looking for something expired in that case
* we would need to track down the parent pack.
* TODO: Init() => Iterate over assets folder and cache *ALL* files into a list.
* */
public class AssetUtils {
    private LinkedHashSet<AssetFile> FileList;
    private Context AndroidContext;
    private AssetManager Manager;

    public AssetFile Get(String Name) {
        long Now = RenderEngine.SysMilliseconds();

        for( AssetFile File : FileList ) {
            if( File.GetName().equals(Name) ) {
                long Delta = RenderEngine.SysMilliseconds() - Now;
                Timber.d("Took " + Delta + " ms to find the file " + Name);
                return File;
            }
        }
        long Delta = RenderEngine.SysMilliseconds() - Now;
        Timber.d("Took " + Delta + " ms to miss the file " + Name);
        return null;
    }

    public AssetFile Open(int ResourceID) throws IOException {
        AssetFile File;
        String FilePath;

        FilePath = ContextUtils.GetStringFromResources(AndroidContext,ResourceID);
        File = new AssetFile(FilePath,Manager.open(FilePath));
        Timber.d("Opening " + File.GetName());
        return File;
    }

    public ArrayList<AssetFile> OpenZip(int ResourceID) throws IOException {
        ArrayList<AssetFile> Result;
        AssetFile ZipFile;
        ZipInputStream ZipStream;
        ZipEntry Entry;

        Result = new ArrayList<>();
        ZipFile = Open(ResourceID);
        ZipStream = new ZipInputStream(ZipFile.GetInputStream());
        while( (Entry = ZipStream.getNextEntry()) != null ) {
            Timber.d("Entry name " + Entry.getName());
            Timber.d("Entry Available Size: " + ZipStream.available());
            Timber.d("Entry Length: " + Entry.getSize());
            if( Entry.isDirectory() ) {
                Timber.d("It's a a dir");
            } else {
                Timber.d("It's a standard file.");

                Result.add(new AssetFile(Entry.getName(),ZipStream));
            }
        }
        ZipStream.close();
        return Result;
    }

    public ArrayList<AssetFile> OpenZip(InputStream Stream) throws IOException {
        ArrayList<AssetFile> Result;
        AssetFile ZipFile;
        ZipInputStream ZipStream;
        ZipEntry Entry;

        Result = new ArrayList<>();
        ZipStream = new ZipInputStream(Stream);
        while( (Entry = ZipStream.getNextEntry()) != null ) {
            Timber.d("Entry name " + Entry.getName());
            Timber.d("Entry Available Size: " + ZipStream.available());
            Timber.d("Entry Length: " + Entry.getSize());
            if( Entry.isDirectory() ) {
                Timber.d("It's a a dir");
            } else {
                Timber.d("It's a standard file.");

                Result.add(new AssetFile(Entry.getName(),ZipStream));
            }
        }
        ZipStream.close();
        return Result;
    }

    /**
     * A Bit Hacky maybe but seems to work...
     * @param Path
     * @return
     */
    public boolean IsDir(String Path) {
        String ExceptionMessage = "";
        try {
            AssetFileDescriptor desc = Manager.openFd(Path);
            desc.close();  // Never executes
        } catch (Exception Exception) {
            ExceptionMessage = Exception.toString();
            Timber.d(ExceptionMessage);
        }

        if (ExceptionMessage.endsWith(Path)) {
            return true;
        } else {
            return false;
        }
    }
    private void CacheFilesRecursive(String Path) {
        String SubDir;
        boolean IsDir;
        boolean IsZip;
        try {
            String[] Files;
            Files = Manager.list(Path);
            for (String FileInDir : Files ) {
                SubDir = Path + "/" + FileInDir;
                IsDir = IsDir(SubDir);
                IsZip = FileInDir.substring(FileInDir.indexOf(".") + 1).equals("zip");
                if( IsDir ) {
                    CacheFilesRecursive(SubDir);
                } else {
                    if( IsZip ) {
                        FileList.addAll(OpenZip(Manager.open(SubDir)));
                    } else {
                        FileList.add(new AssetFile(FileInDir, Manager.open(SubDir)));
                    }
                }
            }
        } catch ( IOException e ) {

        }
    }

    /*
    * TODO: What if a resource directory contains other directories?
    *       How Can We Detect it?
    *       Shall we try to open it / error out?
    *       We can try to list it to see if it returns 0 length
    *       but this could also mean that it is an empty dir not a file...
    *       Maybe 0 length + open it...?
    * */
    private void CacheFiles() {
        String[] DirsToScan;

        DirsToScan = AndroidContext.getResources().getStringArray(R.array.ResourceFileDirs);
        long Start = RenderEngine.SysMilliseconds();
        for( String Dir : DirsToScan ) {
            CacheFilesRecursive(Dir);
        }
        long Delta = RenderEngine.SysMilliseconds() - Start;
        Timber.d("Cached " + FileList.size() + " Files and took " + Delta + " ms.");
        for( AssetFile f : FileList ) {
            Timber.d("Got " + f.GetName());
        }
    }

    private void Init() {
        Manager = AndroidContext.getAssets();
        FileList = new LinkedHashSet<>();
        CacheFiles();
    }

    public AssetUtils(Context AndroidContext) {
        this.AndroidContext = AndroidContext;
        Init();
    }
}
