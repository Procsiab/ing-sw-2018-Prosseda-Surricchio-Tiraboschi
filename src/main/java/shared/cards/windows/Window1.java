package shared.cards.windows;
import shared.Cell;
import shared.abstracts.Window;

public class Window1 extends Window {

    public Window1(){
        setName("Window1");
        Cell[][] cells = new Cell[4][5];

        //assign of cells


        setCells(cells);
    }
}