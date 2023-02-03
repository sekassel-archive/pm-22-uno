package de.uniks.pmws2223.uno.controller;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class PlayerController implements Controller{

    final private Player player;
    private PropertyChangeListener cardListener;
    private GameService gameService;

    public PlayerController(Player player, GameService gameService){
        this.player = player;
        this.gameService = gameService;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public void init() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parent render() throws IOException {
        HBox cardBox;
        Parent parent;
        if(player.isIsBot()){
            VBox parentBox = new VBox();
            parentBox.setMinWidth(333);
            cardBox = new HBox();
            Label botName = new Label();
            botName.setText(player.getName());
            botName.setFont(Font.font("Comic Sans MS", 29));
            botName.setUnderline(true);
            botName.setTranslateX(12);
            cardBox.setMaxWidth(200);
            cardBox.setPrefHeight(96);
            cardBox.setAlignment(Pos.CENTER);
            parentBox.getChildren().add(botName);
            parentBox.getChildren().add(cardBox);
            parentBox.setAlignment(Pos.CENTER);
            parent = parentBox;
        }
        else{
            cardBox = new HBox();
            cardBox.setPrefWidth(380);
            cardBox.setPrefHeight(160);
            cardBox.setLayoutX(317);
            cardBox.setLayoutY(452);
            cardBox.setAlignment(Pos.CENTER);
            cardBox.setScaleX(1.5);
            cardBox.setScaleY(1.5);
            parent = cardBox;
        }

        cardListener = event -> {
            if(event.getNewValue() != null){
                Card newCard = (Card) event.getNewValue();
                Player owner = newCard.getOwner();
                Pane newUICard = gameService.generateUICard(newCard, owner.isIsBot());

                cardBox.getChildren().add(newUICard);
            }
            else if(event.getOldValue() != null){
                cardBox.getChildren().removeIf(card -> card.getUserData().equals(event.getOldValue()));
            }
        };

        player.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS ,cardListener);

        return parent;
    }

    @Override
    public void destroy() {
        player.listeners().removePropertyChangeListener(Player.PROPERTY_CARDS, cardListener);
    }
    
}
