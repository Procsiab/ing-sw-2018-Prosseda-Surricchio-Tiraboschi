package shared;

import javafx.geometry.Pos;

import java.io.Serializable;

public class PositionR implements Serializable {
    private static final long serialVersionUID = 1524857752L;
    private Integer column;
    private Integer height;

    public PositionR() {
    }

    public PositionR(Integer column, Integer height) {
        this.column = column;
        this.height = height;
    }

    public Integer getColumn() {
        return column;
    }

    public Integer getHeight() {
        return height;
    }

    public void setColumn(Integer column) {
        this.column = column;
    }

    public void setHeight(Integer height) {
        this.height = height;
    }

    @Override
    public String toString() {
        return column + ", " + height;
    }
}