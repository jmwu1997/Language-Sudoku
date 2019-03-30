package eta.sudoku;

import android.content.Context;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class VocabBadStorage {
    public void saveList(Object object,String filepath, String listname){
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
    public VocabLibrary loadList(String filepath, String listname){
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
    public VocabLibrary loadLibrary(string)
}
