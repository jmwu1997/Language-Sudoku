package eta.sudoku.model;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eta.sudoku.SudokuApplication;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.view.PuzzleActivity;

public class VocabStorage {
    private static final VocabStorage ourInstance = new VocabStorage();
    private String filepath;
    public VocabStorage(){
    }
    private File getAppDir(){
        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"etaSudoku");
        return dir;
    }
    public String addLang(String language){
        if(isSDCARDAvailable()||isExternalStorageReadOnly()) {
            File dir = new File(getAppDir() + File.separator + language);
            boolean success = true;
            Log.d("LANGADD", dir.toString());

            if (!(dir.exists())) {
                success = dir.mkdirs();
                Log.d("LANGADD", "tried to add language " + language);

            }
            if (success) {
                Log.d("LANGADD", "Successfully added Language");
                setLanguage("language");
                VocabLibrary library = new VocabLibrary();
                saveList(library,"Library");
                return "Successfully added Language";
            } else {
                Log.d("LANGADD", "Failed to add Language");
                return "Failed to add Language";
            }
        }
        else
            return "Storage unavailable";
    }

    public String[] getFiles(){
        File dir= getAppDir();
        return dir.list();
       }
    public String[] getLanguages(){
        File dir=getAppDir() ;
        return dir.list();
    }
    public String[] getWordLists(){

        if(filepath==null){
            String[] langs=getLanguages();
            setLanguage(langs[0]);

        }
        File dir =new File(filepath);
        String[] lists=dir.list();

        if(lists.length==0){
            VocabLibrary library = new VocabLibrary();
            Log.d("ADDLIB","tried to add lib");
            saveList(library,"Library");
        }
        lists=dir.list();
        return lists;
    }
    public void setLanguage(String language){
       filepath= getAppDir()+File.separator+language+File.separator;
    }
    public String getLanguage(){
        if(filepath==null){
            return null;
        }
        int index=filepath.lastIndexOf(File.separator);
        String language = filepath.substring(0,index);
        index=language.lastIndexOf(File.separator);
        language=language.substring(index);
        return language;
    }




    public void saveList(Object object, String listname){
        try{
            FileOutputStream fileOut = new FileOutputStream(filepath+File.separator+listname);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
            fileOut.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public VocabLibrary loadList(String listname){
        try {
            FileInputStream fileIn = new FileInputStream(filepath + listname);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            VocabLibrary lib = (VocabLibrary) objectIn.readObject();
            objectIn.close();
            fileIn.close();
            return lib;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }
    public boolean deleteList(String listName){
        if(listName=="Library"){
            return false;
        }
        File file = new File(filepath+listName);
        return file.delete();
    }
    public boolean deleteLang(String langName){
        File dir = new File(getAppDir()+File.separator +langName);
        if (dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                new File(dir, children[i]).delete();
            }
        }
        return dir.delete();
    }
    public VocabLibrary loadLibrary(){
        return loadList("Library");
    }
    public void saveLibrary(VocabLibrary library){
        saveList(library,"Library");
    }
    public static VocabStorage getInstance(){

        return ourInstance;

    }
    public static boolean isSDCARDAvailable(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }
    private static boolean isExternalStorageReadOnly() {
        String extStorageState = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(extStorageState)) {
            return true;
        }
        return false;
    }
}
