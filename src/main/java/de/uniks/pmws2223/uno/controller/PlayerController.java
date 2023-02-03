package de.uniks.pmws2223.uno.controller;

import java.beans.PropertyChangeListener;
import java.io.IOException;

import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
            cardBox = new HBox();
            Label botName = new Label();
            botName.setText(player.getName());
            cardBox.setPrefWidth(256);
            cardBox.setPrefHeight(96);
            parentBox.getChildren().add(botName);
            parentBox.getChildren().add(cardBox);
            parent = parentBox;
        }
        else{
            cardBox = new HBox();
            cardBox.setPrefWidth(682);
            cardBox.setPrefHeight(159);
            cardBox.setLayoutX(162);
            cardBox.setLayoutY(419);
            parent = cardBox;
        }

        cardListener = event -> {
            if(event.getNewValue() != null){
                Card newCard = (Card) event.getNewValue();
                StackPane newUICard = gameService.generateUICard(newCard);
                Player owner = newCard.getOwner();

                cardBox.getChildren().add(newUICard);
            }
            else if(event.getOldValue() != null){
                //TODO: kill old value from respective hbox
            }
        };

        player.listeners().addPropertyChangeListener(Player.PROPERTY_CARDS ,cardListener);

        return parent;
    }

    @Override
    public void destroy() {
        // TODO Auto-generated method stub
        
    }
    
}
