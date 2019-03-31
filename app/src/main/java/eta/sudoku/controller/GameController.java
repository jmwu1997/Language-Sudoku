package eta.sudoku.controller;

import android.content.Context;
import android.widget.Toast;

import eta.sudoku.model.Game;
import eta.sudoku.model.Puzzle;

public class GameController {
    private static final GameController ourInstance = new GameController();
    private static final PuzzleController puzzleController = PuzzleController.getInstance();
    private static final VocabLibraryController vocabLibController = VocabLibraryController.getInstance();

    private Game mGame;

    public GameController(){

    }
    public void newGame(Puzzle puzzle, boolean isListen){
        mGame = new Game(puzzle, isListen);
    }
    public void swapLang(){
        int temp = mGame.getSelectLang();
        mGame.setSelectLang(mGame.getPuzzleLang());
        mGame.setPuzzleLang(temp);
    }
    public int getSelectLang() {
        return mGame.getSelectLang();
    }

    public void setSelectLang(int selectLang) {
        mGame.setSelectLang(selectLang);
    }

    public int getPuzzleLang() {
        return mGame.getPuzzleLang();
    }

    public void setPuzzleLang(int puzzleLang) {
        mGame.setPuzzleLang(puzzleLang);
    }

    public int getSelectedIndex() {
        return mGame.getSelectedIndex();
    }

    public void setSelectedIndex(int wordInd) {
        mGame.setSelectedIndex(wordInd);
    }

    public void fillCell(int row, int col){
        puzzleController.fillCell(mGame.getSelectedIndex(), row, col);
    }

    public boolean isSolved() {
        if (!puzzleController.isCompleted()) return false;
        for (int i = 0; i < puzzleController.getSize(); i++) {
            if (!puzzleController.isRowSolved(i) || !puzzleController.isColSolved(i) || !puzzleController.isSubSolved(i)) return false;
        }
        return true;
    }

    public boolean isListenMode() {
        return mGame.isListenMode();
    }

    public void setListenMode(boolean listenMode) {
        mGame.setListenMode(listenMode);
    }
    public boolean[] checkDuplicate(int row,int col) {
        boolean rowWrong = puzzleController.isDuplicateInRow(row);
        boolean colWrong = puzzleController.isDuplicateInCol(col);
        boolean subWrong;
        int r = 0;
        int c = 0;
        switch (puzzleController.getSize()) {
            case 4:
            case 9:
                r = (int) Math.sqrt(puzzleController.getSize());
                c = r;
                break;
            case 6:
                r = 2;
                c = 3;
                break;
            case 12:
                r = 3;
                c = 4;
                break;
            default:
                System.out.print("mSize out of range");
                break;
        }
        assert r == 0 || c == 0;
        int sub = (row / r) * r + col / c;
        subWrong = puzzleController.isDuplicateInSub(sub);
        boolean[] result = {rowWrong, colWrong, subWrong};
        return result;
    }
    public boolean isDuplicate(int row, int col){
        boolean[] d = checkDuplicate(row, col);
        if(d[0] || d[1] || d[2]) return true;
        else return false;
    }
    public void incorrectInc(int row, int col){
        mGame.incorrectInc(puzzleController.getFilledCell(row,col));
    }
    public int getIncorrectCount(int row, int col){
        return mGame.getIncorrectCount(puzzleController.getFilledCell(row,col));
    }
    public void destroy(){//TODO:
        mGame = null;
    }
    public static GameController getInstance() {
        return ourInstance;
    }
}
