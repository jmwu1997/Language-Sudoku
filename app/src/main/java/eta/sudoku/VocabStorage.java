package eta.sudoku;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VocabStorage {
    private String filepath;
    public String addLang(String language){
        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+language);
        boolean success=true;
        if(!dir.exists()){
            success= dir.mkdirs();
        }
        if(success){
            return "Successfully added Language";
        }else{
            return "Failed to add Language";
        }
    }
    public String[] getLanguages(){
        File dir=Environment.getExternalStorageDirectory();
        return dir.list();
    }
    public String[] getWordLists(){
        File dir =new File(filepath);
        return dir.list();
    }
    public void setLanguage(String language){
       filepath= Environment.getExternalStorageDirectory().toString()+File.separator+language;
    }
    public void saveList(Object object, String listname){
        try{
            FileOutputStream fileOut = new FileOutputStream(filepath+listname);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
            fileOut.close();

        } catch(Exception ex){
            ex.printStackTrace();
        }
    }
    public VocabLibrary loadList( String listname){
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
    public VocabLibrary loadLibrary(){
        return loadList("Library");
    }
    public void saveLibrary(VocabLibrary library){
        saveList(library,"Library");
    }
}
