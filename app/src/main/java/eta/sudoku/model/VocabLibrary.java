package eta.sudoku.model;


import java.util.ArrayList;
import java.util.Random;

public class VocabLibrary extends ArrayList<Vocab>{
    //private ArrayList<Vocab> mVocabList;
    double difficultWeight = 3;
    double normalWeight = 1;
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
        if(tempSize >= num) {
            for (int i = 0; i < num; i++) {
                int ind = r.nextInt(tempSize)+1;
                result.add(temp.get(ind));
                temp.remove(ind);
                tempSize--;
            }
        }

        return result;
    }
    public VocabLibrary getWeightedRandomVocabs(int num){
        WeightedRandom<Vocab> r = new WeightedRandom<>();
        //ArrayList<Vocab> temp = (ArrayList<Vocab>) this.clone();
        //int tempSize = temp.size()-1; //Considering the first vocab is empty
        for(int i=1; i<size(); i++){
            if(get(i).isDifficult()) r.add(difficultWeight, get(i));
            else r.add(normalWeight, get(i));
        }
        VocabLibrary result = new VocabLibrary();

        while(result.size()<=num+1){
            Vocab temp = r.next();
            result.add(temp);
            r.remove(temp);
        }



        return result;
    }

}
