package de.uniks.pmws2223.uno.model;

import java.util.List;

import org.fulib.builder.ClassModelDecorator;
import org.fulib.builder.ClassModelManager;
import org.fulib.builder.reflect.Link;

public class GenModel implements ClassModelDecorator {

    class Game {
        @Link
        List<Player> bots;

        @Link
        Player human;

        @Link
        List<Card> discardCards;

        @Link
        List<Card> drawCards;
    }

    class Player {
        String name;

        @Link("nextPlayer")
        Player previousPlayer;
        
        @Link("previousPlayer")
        Player nextPlayer;

        @Link("owner")
        List<Card> cards;
    }

    class Card {
        int number;
        
        String color;
        
        String type;

        @Link("cards")
        Player owner;
    }

    @Override
    public void decorate(ClassModelManager mm) {
		mm.getClassModel().setDefaultPropertyStyle(org.fulib.builder.Type.BEAN);
		mm.haveNestedClasses(getClass());
    }
}
