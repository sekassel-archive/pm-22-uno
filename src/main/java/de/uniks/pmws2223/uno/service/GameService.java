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
            game.withBots(new Player().setName("Bot0"+_i));
        }

        //Generate Player
        game.setHuman(new Player().setName(playerName));

        //Set Order
        game.getHuman().setNextPlayer(game.getBots().get(0));
        for(Player bot: game.getBots()){
            if(game.getBots().indexOf(bot) >= botCount-1){
                bot.setNextPlayer(game.getHuman());
            }
            else{
                bot.setNextPlayer(game.getBots().get(game.getBots().indexOf(bot)+1));
            }
        }
        return game;
    }

    public void dealStartingCards(Game game, ArrayList<Card> deck){
        for(int _i = 0; _i < 7; _i++){
            game.getHuman().withCards(deck.remove(0));
            for(Player bot : game.getBots()){
                bot.withCards(deck.remove(0));
            }
        }
    }

    
}
