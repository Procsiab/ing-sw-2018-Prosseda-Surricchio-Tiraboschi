package server.cards;

import server.Dice;
import server.Player;
import server.Position;
import server.abstracts.PrivateOC;
import server.threads.GameManager;

public class ToolC2 extends PrivateOC {

    private String name = "2";
    private String description = null;


    public boolean use(Player player, GameManager game, Position position1, Position position2){
        //move any one die in your window regardless only of color restriction.
        Dice dice = player.frame.getDicePositions()[position1.row][position1.column];
        //check if position is not empty, else return
        if(dice == null)
            return false;
        //move the die
        player.frame.getDicePositions()[position1.row][position1.column] = null;
        player.frame.checkDicePositions1(dice, position2, player);
        return true;
    }
}
