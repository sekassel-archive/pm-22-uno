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
        NORMAL,
        REVERSE,
        SKIP,
        DRAW_TWO,
        WILD,
        WILD_DRAW_FOUR
    };

    public static final List<Card> UNO_DECK = new ArrayList<Card>();

    static{
        //Normal Cards
        for(int _i = 0; _i < 10; _i++){
            for(CARD_COLOR color : CARD_COLOR.values()){
                UNO_DECK.add(new Card().setColor(color.getColor().toString()).setNumber(_i).setType(CARD_TYPE.NORMAL.toString()));
                UNO_DECK.add(new Card().setColor(color.getColor().toString()).setNumber(_i).setType(CARD_TYPE.NORMAL.toString()));
            }
        }   

        //Special Cards - for every color, 2 special cards
        for(CARD_COLOR color : CARD_COLOR.values()){
            for(CARD_TYPE type : CARD_TYPE.values()){
                if(type != CARD_TYPE.NORMAL && type != CARD_TYPE.WILD && type != CARD_TYPE.WILD_DRAW_FOUR){
                    UNO_DECK.add(new Card().setColor(color.getColor().toString()).setNumber(-1).setType(type.toString()));
                    UNO_DECK.add(new Card().setColor(color.getColor().toString()).setNumber(-1).setType(type.toString()));
                }
            }
        }

        //Black Wild Cards
        for(int _j = 0; _j < 4; _j++){
            UNO_DECK.add(new Card().setColor(null).setNumber(-1).setType(CARD_TYPE.WILD.toString()));
            UNO_DECK.add(new Card().setColor(null).setNumber(-1).setType(CARD_TYPE.WILD_DRAW_FOUR.toString()));
        }
    }
}
