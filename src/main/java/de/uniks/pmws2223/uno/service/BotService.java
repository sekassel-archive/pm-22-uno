package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Player;

public class BotService {
    final GameService gameService;

    public BotService(GameService gameService){
        this.gameService = gameService;
    }

    public Card checkCardsForPlayable(Player bot){
        for(Card card : bot.getCards()){
            if(gameService.cardIsPlayable(card, bot.getCurrentGame().getDiscardCards().get(bot.getCurrentGame().getDiscardCards().size()-1))){
                return card;
            }
        }
        return null;
    }
}
