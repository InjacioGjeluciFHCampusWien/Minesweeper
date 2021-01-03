package at.ac.fhcampuswien;

import javafx.scene.image.Image;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Board {
    public static final int CELL_SIZE = 15;
    public static final int ROWS = 8;
    public static final int COLS = 8;
    public static final int NUM_IMAGES = 13;
    public static final int NUM_MINES = 5;

    // Add further constants or let the cell keep track of its state.

    private Cell cells[][];
    private Image[] images;
    private int cellsUncovered;
    private int minesMarked;
    private boolean gameOver;
    private Cell[] emptycells;
    private int emptyNeighbourCounter;

    /**
     * Constructor preparing the game. Playing a new game means creating a new Board.
     */
    public Board(){
        emptycells = new Cell[ROWS*COLS-NUM_MINES+1];
        cells = new Cell[ROWS][COLS];
        cellsUncovered = 0;
        minesMarked = 0;
        gameOver = false;
        emptyNeighbourCounter=0;
        loadImages();
        // at the beginning every cell is covered
        // TODO cover cells. complete the grid with calls to new Cell();

        for(int i=0; i < Board.ROWS; i++){
            for(int j = 0; j<Board.COLS; j++){
                cells[i][j]= new Cell(this.images[10], ImageState.CLOSED.ordinal());
            }
        }

        // set neighbours for convenience
        // TODO compute all neighbours for a cell.
        for(int i = 0; i< Board.ROWS; i++){
            for(int j = 0; j< Board.COLS; j++){
                cells[i][j].setNeighbours(this.computeNeighbours(i,j));
            }
        }

        // then we place NUM_MINES on the board and adjust the neighbours (1,2,3,4,... if not a mine already)
        // TODO place random mines.
        int counter = 0;
        while(counter<NUM_MINES){
            int x = this.getRandomNumberInts(0,ROWS-1);
            int y = this.getRandomNumberInts(0,COLS-1);



            if(!cells[x][y].getIsMine()){
                counter++;
                cells[x][y].setIsMine(true);
            }
            for (Cell neighbour:
                    cells[x][y].getNeighbours()) {
                neighbour.setMinesNear(neighbour.getMinesNear()+1);
            }
        }
    }

    public boolean uncover(int row, int col) {
        if(this.cells[row][col].getIsMine()){
            this.gameOver = true;
        }else {
            if (this.cells[row][col].getMinesNear() == 0) {

                this.uncoverEmptyCells(this.cells[row][col]);



            } else {
                this.cells[row][col].update(images[this.cells[row][col].getMinesNear()]);

                this.cells[row][col].setVisited(true);
                this.cells[row][col].setState(ImageState.OPENED.ordinal());
                cellsUncovered++;
            }
        }

        return true;
    }

    public boolean markCell(int row, int col) {
        // TODO mark the cell if it is not already marked.
        if(this.cells[row][col].getState() == ImageState.OPENED.ordinal()){
            return false;
        }

        if(this.cells[row][col].getState() == ImageState.MARKED.ordinal()){
            this.cells[row][col].update(images[10]);
            this.minesMarked--;
            this.cells[row][col].setState(ImageState.CLOSED.ordinal());
        }else{
            this.cells[row][col].update(images[11]);
            this.minesMarked++;
            this.cells[row][col].setState(ImageState.MARKED.ordinal());
        }

        return true;
    }

    public void uncoverEmptyCells(Cell cell) {
       // TODO you may implement this function. It's usually implemented by means of a recursive function.

        this.traverseEmptyCellsNearby(cell);
        cell.setVisited(true);
        cell.update(images[0]);
        this.cellsUncovered++;
        for (Cell emptycell: this.emptycells) {
            this.traverseEmptyCellsNearby(emptycell);
            emptycell.setVisited(true);

        }
    }


    public void uncoverAllCells(){
        for (int i = 0; i<ROWS;i++){
            for(int j = 0; j<COLS; j++){
                if(cells[i][j].getState()==ImageState.MARKED.ordinal() && !cells[i][j].getIsMine()){
                    cells[i][j].update(images[12]);

                }
                else if(cells[i][j].getState()==ImageState.CLOSED.ordinal() && cells[i][j].getIsMine()) {
                    cells[i][j].update(images[9]);

                }else if(cells[i][j].getState()==ImageState.MARKED.ordinal() && cells[i][j].getIsMine()){
                    continue;
                }
                else{
                    cells[i][j].update(images[cells[i][j].getMinesNear()]);

                }


                cells[i][j].setState(ImageState.OPENED.ordinal());

            }
        }
    }


    public List<Cell> computeNeighbours(int x, int y){
        List<Cell> neighbours = new ArrayList<>();
        // TODO get all the neighbours for a given cell. this means coping with mines at the borders.
        int[] points = new int[]{
                -1,-1,
                -1,0,
                -1,1,
                0,-1,
                0,1,
                1,-1,
                1,0,
                1,1

        };
        for(int i = 0; i <  points.length; i++){
            int dx =points[i];
            int dy = points[++i];

            int newX = x + dx;
            int newY = y + dy;

            if(newX >= 0 && newX < Board.COLS && newY >= 0 && newY <Board.ROWS){
                neighbours.add(cells[newX][newY]);
            }
        }

        return neighbours;
    }

    /**
     * Loads the given images into memory. Of course you may use your own images and layouts.
     */
    private void loadImages(){
        images = new Image[NUM_IMAGES];
        for(int i = 0; i < NUM_IMAGES; i++){
            var path = "src/res/" + i + ".png";
            FileInputStream fis = null;
            try {
                fis = new FileInputStream(path);
                this.images[i] = new Image(fis);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public Cell[][] getCells() {
        return cells;
    }

    /**
     * Computes a random int number between min and max.
     * @param min the lower bound. inclusive.
     * @param max the upper bound. inclusive.
     * @return a random int.
     */
    private int getRandomNumberInts(int min, int max){
        Random random = new Random();
        return random.ints(min,(max+1)).findFirst().getAsInt();
    }

    public int getMinesMarked() {
        return minesMarked;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getCellsUncovered() {
        return cellsUncovered;
    }

    private void traverseEmptyCellsNearby(Cell cell){


        for (Cell neighbour : cell.getNeighbours()) {
            if(!neighbour.isVisited()){
                neighbour.update(images[neighbour.getMinesNear()]);
                if(neighbour.getState()!=ImageState.OPENED.ordinal()){
                    this.cellsUncovered++;
                }
                neighbour.setState(ImageState.OPENED.ordinal());
                if(neighbour.getMinesNear()==0 && !neighbour.isInserted()){
                    this.emptycells[emptyNeighbourCounter++] = neighbour;
                    neighbour.setInserted(true);
                }
            }

        }
    }

}
