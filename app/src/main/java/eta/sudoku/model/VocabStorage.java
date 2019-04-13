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
import java.io.FilenameFilter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eta.sudoku.SudokuApplication;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.view.VocabActivity;

public class VocabStorage {
    private static final VocabStorage ourInstance = new VocabStorage();
    private String filepath;
    public boolean checkPermission(Context ctx){
        if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity)ctx,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},0);
            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
            // Permission is not granted
        }
        return true;
    }

    public VocabStorage(){
    }

    private File getAppDir(){
        File dir = new File(Environment.getExternalStorageDirectory()+File.separator+"etaSudoku");
        return dir;
    }

    public boolean addLang(String language){
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
                return true;
            } else {
                Log.d("LANGADD", "Failed to add Language");
                return false;
            }
        }
        else
            return false;
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
        int fileend=filepath.lastIndexOf(File.separator);

        File dir =new File(filepath.substring(0,fileend));
        String[] lists=dir.list();

        if(lists==null){
            VocabLibrary library = new VocabLibrary();
            Log.d("ADDLIB","tried to add lib");
            saveList(library,"Library");
        }
        lists=dir.list();
        return lists;
    }

    public void setLanguage(String language){
        String newPath= getAppDir()+File.separator+language;
        File dir = new File(newPath);
        boolean success=true;
        if (!(dir.exists())) {
            success= addLang(language);
        }
        if(success){
            filepath=newPath+File.separator;
        }


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

    public void exportList(VocabLibrary lib){
        VocabLibraryController controller= VocabLibraryController.getInstance();
        int size = controller.getGameVocabListSize();
        String[] splitPath=filepath.split(File.separator);
        String cvs=splitPath[splitPath.length-1]+"," +size;
        for(int i=0;i<size;i++){
            cvs+=","+controller.getGameVocab(i,0)+","+controller.getGameVocab(i,1);
        }
        String tfile=filepath;
        filepath=Environment.getExternalStorageState()+File.separator+"Download";
        saveList(cvs,controller.getName()+".cvs");
        filepath=tfile;
    }
    public String[] importableLists(){
        File dir=new File(Environment.getExternalStorageState()+File.separator+"Download");
                String[] list = dir.list(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.matches("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+(.cvs)$");
            }
        });
                return list;
    }
    public VocabLibrary importList(String list){
        File dir=new File(Environment.getExternalStorageState()+File.separator+"Download"+File.separator);
        VocabLibrary vlib=new VocabLibrary();
        String lib;
        try {
            FileInputStream fileIn = new FileInputStream(dir + list);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            lib = (String) objectIn.readObject();
            objectIn.close();
            fileIn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        String[] els=lib.split(",");
        int size= Integer.parseInt(els[1]);
        String language=els[0];
        setLanguage(language);
        VocabLibraryController.getInstance().setName(list.split("(.cvs)$")[0]);
        Vocab v= new Vocab();
        for(int i=2;i<size+2;i++){
            v.setWord(0,els[i]);
            v.setWord(1,els[i+1]);
            vlib.add(v);
        }
        return vlib;
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
