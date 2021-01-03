package at.ac.fhcampuswien;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.util.List;

public class Cell extends Pane {
    private ImageView view;
    private List<Cell> neighbours;
    private boolean isMine = false;
    private int state;
    private int minesNear;
    private boolean visited;
    private boolean inserted;

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    // TODO add addtional variables you need. for state...

    public boolean isInserted() {
        return inserted;
    }

    public void setInserted(boolean inserted) {
        this.inserted = inserted;
    }

    public Cell(Image img, int state) {
        view = new ImageView(img);
        getChildren().add(view);
        // TODO add stuff here if needed.
        this.state = state;
        this.minesNear=0;
        this.visited = false;
        this.inserted=false;
    }

    public void setNeighbours(List<Cell> neighbours) {
        this.neighbours = neighbours;
    }

    public List<Cell> getNeighbours() {
        return neighbours;
    }

    public void update(Image img) {
        this.view.setImage(img);
    }

    public boolean getIsMine(){
        return this.isMine;
    }

    public void setIsMine(boolean isMine){
        this.isMine = isMine;
    }

    public int getState(){
        return this.state;
    }

    public void setState(int state){
        this.state = state;
    }

    public int getMinesNear() {
        return minesNear;
    }

    public void setMinesNear(int minesNear) {
        this.minesNear = minesNear;
    }





}
