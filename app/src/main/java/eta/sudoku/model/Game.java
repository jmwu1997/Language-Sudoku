package eta.sudoku.model;

import android.util.Log;

import java.util.Stack;

import eta.sudoku.controller.PuzzleController;

public class Game {
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private int selectLang;
    private int puzzleLang;
    private int selectedIndex;
    private int[] incorrectCount;
    private boolean isListenMode;
    private Stack<PuzzleInput> mRedoHistory = new Stack<>();
    private Stack<PuzzleInput> mUndoHistory = new Stack<>();
    private int challengeDifficulty;
    private boolean isChallengeMode;

    public Game(Puzzle puzzle, boolean isListen, boolean isChallenge, int challengeDifficulty_){
        selectLang = 1;
        puzzleLang = 0;
        selectedIndex = -1;
        incorrectCount = new int[puzzle.getSize()];
        isListenMode = isListen;
        challengeDifficulty = challengeDifficulty_;
        isChallengeMode = isChallenge;
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

    public boolean isChallenge() { return isChallengeMode;}

    public int getChallengeDifficulty() {
        return challengeDifficulty;
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

    public void pushRedoHistory(PuzzleInput input) {
        mRedoHistory.push(input);
    }
    public PuzzleInput popRedoHistory(){
        return mRedoHistory.pop();
    }
    public PuzzleInput peekRedoHistory(){
        return mRedoHistory.peek();
    }
    public PuzzleInput peekUndoHistory(){
        return mUndoHistory.peek();
    }
    public void pushUndoHistory(PuzzleInput undo){
        mUndoHistory.push(undo);
    }
    public PuzzleInput popUndoHistory(){
        return mUndoHistory.pop();
    }
    public boolean isUndoHistoryEmpty() {
        return mUndoHistory.isEmpty();
    }
    public boolean isRedoHistoryEmpty(){
        return mRedoHistory.isEmpty();
    }

    public void newRedoHistory(){
        mRedoHistory = new Stack<>();
    }
    public Stack<PuzzleInput> getUndoHistory() {
        return mUndoHistory;
    }
}
