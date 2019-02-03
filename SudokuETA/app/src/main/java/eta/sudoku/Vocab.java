package eta.sudoku;

public class Vocab {
    //Class of words in different languages with same meaning
    private int mNumLang=2; //# of languages(unused)
    private String mWords[]; //an array of words w/ same meaning

    public Vocab(String[] words){ setWords(words);} //construct the class with a complete tuple of same meaning words
    public Vocab() {} //or set words later

    public String[] getWords() { return mWords; } //get the tuple as a whole
    public void setWords(String[] words) { mWords = words; } //set a tuple
    public String getWord(int lang) { return mWords[lang]; } //get the word in specific language with language index
    public void setWord(int lang, String word){ mWords[lang] = word; } //set a word in specific language with language index

}
