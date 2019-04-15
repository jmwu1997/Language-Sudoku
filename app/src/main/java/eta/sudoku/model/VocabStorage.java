package eta.sudoku.model;

import android.Manifest;
import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import eta.sudoku.SudokuApplication;
import eta.sudoku.controller.VocabLibraryController;
import eta.sudoku.model.VocabLibrary;
import eta.sudoku.view.VocabActivity;

import static android.content.Context.DOWNLOAD_SERVICE;

public class VocabStorage {
    private static final VocabStorage ourInstance = new VocabStorage();
    private String filepath;

    //TBD updateLibrary() updates library by adding all words from all other word lists that aren't in library
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
        if(!dir.exists()) {
            dir.mkdir();
        }
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
                setLanguage(language);
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
        String[] langs=dir.list();
        if(langs.length==0){
            initialSetup();
        }
        langs=dir.list();
        return langs;
    }
    private void initialSetup(){
        VocabLibraryController controller=VocabLibraryController.getInstance();
        VocabLibrary lib= controller.getOverallVocabLib();
        addLang("Chinese");
        setLanguage("Chinese");
        controller.setName("Fruit");
        exportList(lib);
        controller.setName("Library");
        saveList(lib, "Library");
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
            return "";
        }
        int index=filepath.lastIndexOf(File.separator);
        String language = filepath.substring(0,index);
        index=language.lastIndexOf(File.separator);
        language=language.substring(index+1);
        return language;
    }

    public void saveList(Object object, String listname){
        try{
            if(filepath==null) {
                String[] langs=ourInstance.getLanguages();
                langs=ourInstance.getLanguages();
                ourInstance.setLanguage(langs[0]);
            }
            FileOutputStream fileOut = new FileOutputStream(filepath+listname);
            ObjectOutputStream objectOut = new ObjectOutputStream(fileOut);
            objectOut.writeObject(object);
            objectOut.close();
            fileOut.close();
            Log.d("SAVE","list saved");
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
            Log.d("LOAD",listname+" loaded");
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
        if(filepath==null) {
        String[] langs=ourInstance.getLanguages();
        langs=ourInstance.getLanguages();
        ourInstance.setLanguage(langs[0]);
        }
        VocabLibraryController controller= VocabLibraryController.getInstance();
        int size = controller.getOverallVocabLibSize();
        String[] splitPath=filepath.split(File.separator);
        String cvs=splitPath[splitPath.length-1]+"," +size;
        FileOutputStream writer;
        for(int i=0;i<size;i++){
            cvs+=","+controller.getOverallVocab(i,0)+","+controller.getOverallVocab(i,1);
        }
        String tfile=filepath;
        filepath=Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+File.separator+controller.getName()+".txt";
//        saveList(cvs,controller.getName()+".txt");
        Log.d("EXPORTLIST", "EXPORTED TO "+filepath);

        try{
            writer=new FileOutputStream(filepath);
            writer.write(cvs.getBytes());
            writer.close();
        }catch(Exception e){

        }
        Log.d("EXPORTLIST",cvs);
//        String[] path=new String[1];
//        path[0]=new File(filepath).getAbsolutePath();
//        String[] types=new String[1];
//        types[0]="text/plain";
//        MediaScannerConnection.scanFile(SudokuApplication.getAppContext(), path, null, new MediaScannerConnection.OnScanCompletedListener() {
//            @Override
//            public void onScanCompleted(String path, Uri uri) {
//                Log.d("kill me","file"+path+"was created maybe"+uri);
//            }
//        });
//        File file=new File(filepath);
//        DownloadManager downloadManager = (DownloadManager) SudokuApplication.getAppContext().getSystemService(DOWNLOAD_SERVICE);
//
//        downloadManager.addCompletedDownload(file.getName(), file.getName(), true, "text/plain",file.getAbsolutePath(),file.length(),true);
        filepath=tfile;

    }
    public String[] importableLists(){
        File dir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
                String[] list = dir.list(new FilenameFilter() {
                    @Override
                    public boolean accept(File dir, String name) {
                        return name.matches("([a-zA-Z0-9\\s_\\\\.\\-\\(\\):])+[.txt|.csv]$");
                    }
                });
                return list;
    }
    public VocabLibrary importList(String list){
        File dir=new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString()+File.separator);
        VocabLibrary vlib=new VocabLibrary();
        String lib;
        try {
            FileInputStream fileIn = new FileInputStream(dir + File.separator+ list);
            InputStreamReader objectIn = new InputStreamReader(fileIn);
            BufferedReader bufferedReader=new BufferedReader(objectIn);
            StringBuilder stringBuilder = new StringBuilder();
            while((lib=bufferedReader.readLine())!=null) {
                stringBuilder.append(lib);
            }
            objectIn.close();
            fileIn.close();
            lib=stringBuilder.toString();
            bufferedReader.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
        String[] els=lib.split(",");
        int size= Integer.parseInt(els[1]);
        Log.d("IMPORTLIST",Integer.toString(size));
        String language=els[0];
        setLanguage(language);
        VocabLibraryController.getInstance().setName(list.split("[.]")[0]);
        Vocab v= new Vocab();
        for(int i=4;i<2*size+2;i+=2){
            v.setWord(0,els[i]);
            v.setWord(1,els[i+1]);
            Log.d("IMPORTLIST",Integer.toString(i)+els[i]+":"+els[i+1]);
            vlib.add(v);
            v=new Vocab();
        }
        saveList( vlib,VocabLibraryController.getInstance().getName());
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
