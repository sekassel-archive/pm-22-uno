package de.uniks.pmws2223.uno.controller;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.BotService;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import static de.uniks.pmws2223.uno.Constants.*;

public class IngameController implements Controller{

    private final App app;
    private final Game game;
    private final BotService botService = new BotService();
    private final GameService gameService;
    private List<Card> deck;
    private PropertyChangeListener cardListener;

    public IngameController(App app, Game game, GameService gameService){
        this.app = app;
        this.game = game;
        this.gameService = gameService;
    }

    @Override
    public String getTitle() {
        return INGAME_TITLE;
    }

    @Override
    public void init() {
        deck = gameService.generateDeck();
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/Ingame.fxml"));
        final HBox playerCards = (HBox) parent.lookup("#cardsPlayer");
        final HBox bot1Cards = (HBox) parent.lookup("#cardsBot1");
        final HBox bot2Cards = (HBox) parent.lookup("#cardsBot2");
        final HBox bot3Cards = (HBox) parent.lookup("#cardsBot3");
        final List<HBox> botCards = List.of(bot1Cards,bot2Cards,bot3Cards);

        cardListener = event -> {
            if(event.getNewValue() != null){
                Card newCard = (Card) event.getNewValue();
                StackPane newUICard = generateUICard(newCard);
                Player owner = newCard.getOwner();
                if(owner == game.getHuman()){
                    playerCards.getChildren().add(newUICard);
                }
                else{
                    int botNr = game.getBots().indexOf(owner);
                    botCards.get(botNr).getChildren().add(newUICard);
                }
            }
            else if(event.getOldValue() != null){
                //TODO: kill old value from respective hbox
            }
        };

        game.getHuman().listeners().addPropertyChangeListener(Player.PROPERTY_CARDS ,cardListener);
        for(Player bot: game.getBots()){
            bot.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS ,cardListener);
        }

        gameService.dealStartingCards(game, (ArrayList<Card>) deck);

        return parent;
    }

    @Override
    public void destroy() {

    }

    private StackPane generateUICard(Card card){
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
