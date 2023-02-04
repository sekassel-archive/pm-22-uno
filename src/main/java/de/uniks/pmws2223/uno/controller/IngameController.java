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
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
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
    private final List<PlayerController> playerControllers = new ArrayList<>();
    private PropertyChangeListener discardListener;
    private Pane wishColorParent;

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
        //deck = gameService.generateDeck();
        //deck = new ArrayList<Card>(game.getDrawCards());
    }

    @Override
    public Parent render() throws IOException {
        final Parent parent = FXMLLoader.load(Main.class.getResource("view/Ingame.fxml"));
        wishColorParent = (Pane) parent.lookup("#wishColorParent");
        for(Node colorRec : wishColorParent.getChildren()){
            colorRec.setOnMouseClicked(this::wishColor);
        }

        for(Player player : game.getPlayers()){
            PlayerController playerController = new PlayerController(player, gameService, player.isIsBot() ? null : wishColorParent);
            playerController.init();
            playerControllers.add(playerController);

            if(player.isIsBot()){
                HBox root = (HBox) parent.lookup("#botBox");
                root.getChildren().add(playerController.render());
            }
            else{
                Pane root = (Pane) parent.lookup("#rootNode");
                root.getChildren().add(playerController.render());
            }
        }

        StackPane drawPile = (StackPane) parent.lookup("#stackDrawPile");
        StackPane discardPile = (StackPane) parent.lookup("#stackDiscardPile");
        gameService.dealStartingCards(game, drawPile, discardPile);

        discardListener = evt -> {
            if (evt.getNewValue() != null) {
                discardPile.getChildren().add(gameService.generateUICard((Card) evt.getNewValue(), false));
                gameService.passTurn();
            }
        };

        game.listeners().addPropertyChangeListener(Game.PROPERTY_DISCARD_CARDS, discardListener);

        return parent;
    }

    private void wishColor(MouseEvent mouseEvent){
        Rectangle rec = (Rectangle) mouseEvent.getSource();
        for(Node recs : wishColorParent.getChildren()){
            ((Rectangle) recs).setStrokeWidth(1);
        }
        rec.setStrokeWidth(4);
        for(Player player : game.getPlayers()){
            player.setWishedColor(rec.getFill().toString());
        }
    } 

    @Override
    public void destroy() {
        game.listeners().removePropertyChangeListener(Game.PROPERTY_DISCARD_CARDS, discardListener);
    }
    
}
