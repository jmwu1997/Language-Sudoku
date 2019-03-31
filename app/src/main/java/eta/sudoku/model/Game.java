package eta.sudoku.model;

import android.util.Log;

import eta.sudoku.controller.PuzzleController;

public class Game {
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private int selectLang;
    private int puzzleLang;
    private int selectedIndex;
    private int[] incorrectCount;
    private boolean isListenMode;

    public Game(Puzzle puzzle, boolean isListen){
        selectLang = 1;
        puzzleLang = 0;
        selectedIndex = -1;
        incorrectCount = new int[puzzle.getSize()];
        isListenMode = isListen;
    }

    public int getSelectLang() {
        return selectLang;
    }

    public void setSelectLang(int lang) {
        selectLang = lang;
    }

    public int getPuzzleLang() {
        return puzzleLang;
    }

    public void setPuzzleLang(int lang) {
        puzzleLang = lang;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int wordIndex) {
        selectedIndex = wordIndex;
    }
    public void incorrectInc(int word){
        incorrectCount[word-1]++;
    }
    public int getIncorrectCount(int word){
        return incorrectCount[word-1];
    }

    public boolean isListenMode() {
        return isListenMode;
    }

    public void setListenMode(boolean listenMode) {
        isListenMode = listenMode;
    }
}
