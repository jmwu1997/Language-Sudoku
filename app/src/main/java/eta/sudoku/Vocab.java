package eta.sudoku;
//model class
public class Vocab {
    //Class of words in different languages with same meaning
    private int mNumLang=2; //# of languages
    private String mWords[] = new String[mNumLang]; //an array of words w/ same meaning



    private int soundFile = 0;

    public Vocab(String[] words, int soundFile){ setWords(words);} //construct the class with a complete tuple of same meaning words
    public Vocab(String[] words){ setWords(words);} //construct the class with a complete tuple of same meaning words
    public Vocab() {} //or set words later

    public int getSoundFile() {
        return this.soundFile;
    }
    public void setSoundFile(int soundFile) {
        this.soundFile = soundFile;
    }
    public String[] getWords() { return this.mWords; } //get the tuple as a whole
    public void setWords(String[] words) { this.mWords = words; } //set a tuple
    public String getWord(int lang) { return this.mWords[lang]; } //get the word in specific language with language index
    public void setWord(int lang, String word){ this.mWords[lang] = word; } //set a word in specific language with language index
    public boolean isSoundMissing(){
        return soundFile == 0;
    }

}
