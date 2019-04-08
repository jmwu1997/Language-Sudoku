package eta.sudoku.model;

public class PuzzleInput {
    private int row;
    private int col;
    private int word;

    public PuzzleInput(int row, int col, int word) {
        this.row = row;
        this.col = col;
        this.word = word;
    }
    public int[] getInput(){
        int[] in = {row,col,word};
        return in;
    }
}
