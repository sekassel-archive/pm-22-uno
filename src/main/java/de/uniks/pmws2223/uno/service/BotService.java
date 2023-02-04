package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.Constants;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Player;

import java.util.Collections;
import java.util.List;

public class BotService {
    final GameService gameService;

    public BotService(GameService gameService){
        this.gameService = gameService;
    }

    public Card checkCardsForPlayable(Player bot, String type){
        for(Card card : bot.getCards()){
            if((type == null || card.getType().equals(type))  && gameService.cardIsPlayable(card, bot.getCurrentGame().getDiscardCards().get(bot.getCurrentGame().getDiscardCards().size()-1))){
                return card;
            }
        }
        return null;
    }

    public String wishBotColor(Player bot) {
        int yellowCount = 0;
        int redCount = 0;
        int blueCount = 0;
        int greenCount = 0;
        for (Card card : bot.getCards()) {
            if (card.getColor() != null) {
                if (card.getColor().equals(Constants.CARD_COLOR.YELLOW.getColor().toString())) yellowCount++;
                if (card.getColor().equals(Constants.CARD_COLOR.RED.getColor().toString())) redCount++;
                if (card.getColor().equals(Constants.CARD_COLOR.BLUE.getColor().toString())) blueCount++;
                if (card.getColor().equals(Constants.CARD_COLOR.GREEN.getColor().toString())) greenCount++;
            }
        }
        int max = Collections.max(List.of(yellowCount, redCount, blueCount, greenCount));
        if (yellowCount ==  max) return Constants.CARD_COLOR.YELLOW.getColor().toString();
        else if (redCount ==  max) return Constants.CARD_COLOR.RED.getColor().toString();
        else if (blueCount ==  max) return Constants.CARD_COLOR.BLUE.getColor().toString();
        else return Constants.CARD_COLOR.GREEN.getColor().toString();
    }
}
