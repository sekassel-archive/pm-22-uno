package de.uniks.pmws2223.uno;

import java.util.ArrayList;
import java.util.List;

import de.uniks.pmws2223.uno.model.Card;
import javafx.scene.paint.Color;

public class Constants {

    public static final String SETUP_TITLE = "Uno - Setup";
    public static final String INGAME_TITLE = "Uno - Ingame";
    public static final String GAMEOVER_TITLE = "Uno - Game Over";

    public enum CARD_COLOR{
        RED(Color.RED),
        BLUE(Color.BLUE),
        GREEN(Color.GREEN),
        YELLOW(Color.YELLOW);

        private final Color myColor;

        private CARD_COLOR(Color color){
            myColor = color;
        }

        public Color getColor(){
            return myColor;
        }
    };

    public enum CARD_TYPE{
        REVERSE,
        SKIP,
        DRAW_TWO,
        WILD,
        WILD_DRAW_FOUR
    };

    public static final List<Card> UNO_DECK = new ArrayList<Card>();

    
}
