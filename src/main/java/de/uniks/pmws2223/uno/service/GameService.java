package de.uniks.pmws2223.uno.service;

import static de.uniks.pmws2223.uno.Constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

public class GameService {

    public List<Card> generateDeck(){
        ArrayList<Card> deck = new ArrayList<Card>(UNO_DECK);
        Collections.shuffle(deck);
        return deck;
    }

    public Game generateGame(int botCount, String playerName){
        Game game = new Game();

        //Generate Bots
        for(int _i = 0; _i < botCount; _i++){
            game.withPlayers(new Player().setName("Bot0"+_i).setIsBot(true));
        }

        //Generate Player
        game.withPlayers(new Player().setName(playerName).setIsBot(false));

        //Set Order
        for(int _i = 0; _i < game.getPlayers().size(); _i++){
            Player player = game.getPlayers().get(_i);
            player.setNextPlayer(game.getPlayers().get((_i+1) % (game.getPlayers().size()-1)));
        }

        return game;
    }

    public void dealStartingCards(Game game, ArrayList<Card> deck){
        for(int _i = 0; _i < 7; _i++){
            for(Player player : game.getPlayers()){
                player.withCards(deck.remove(0));
            }
        }
    }

    public StackPane generateUICard(Card card){
        StackPane UIcard = new StackPane();
        Rectangle rec = new Rectangle();
        rec.setWidth(64);
        rec.setHeight(96);

        //Color color = Color.web(card.getColor(), 1);
        if (card.getColor() != null) {
            rec.setFill(Paint.valueOf(card.getColor()));
        } else {
            rec.setFill(Color.BLACK);
        }

        Label val = new Label();
        val.setText(""+card.getNumber());
        
        UIcard.getChildren().add(rec);
        UIcard.getChildren().add(val);
        return UIcard;
    }
}
