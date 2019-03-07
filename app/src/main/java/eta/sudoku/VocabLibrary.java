package eta.sudoku;


import java.util.ArrayList;
import java.util.Random;

public class VocabLibrary extends ArrayList<Vocab>{
    //private ArrayList<Vocab> mVocabList;

    public VocabLibrary(){
        //mVocabList = new ArrayList<>(1);
        this.add(new Vocab());
        this.get(0).setWord(0,"");
        this.get(0).setWord(1,"");
    }

    public VocabLibrary getRandomVocabs(int num){
        Random r = new Random();
        ArrayList<Vocab> temp = (ArrayList<Vocab>) this.clone();
        int tempSize = temp.size()-1; //Considering the first vocab is empty

        VocabLibrary result = new VocabLibrary();
        if(tempSize >= 9) {
            for (int i = 0; i < num; i++) {
                int ind = r.nextInt(tempSize)+1;
                result.add(temp.get(ind));
                temp.remove(ind);
                tempSize--;
            }
        }

        return result;
    }


}
