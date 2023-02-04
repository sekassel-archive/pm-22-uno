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
    private final BotService botService;
    private final GameService gameService;
    private List<Card> deck;
    private final List<PlayerController> playerControllers = new ArrayList<>();
    private PropertyChangeListener discardListener;
    private Pane wishColorParent;

    public IngameController(App app, Game game, GameService gameService){
        this.app = app;
        this.game = game;
        this.gameService = gameService;
        this.botService = new BotService(gameService);
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
            PlayerController playerController = new PlayerController(player, gameService, player.isIsBot() ? null : wishColorParent, botService);
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
                //check for victory
                if (game.getCurrentPlayer().getCards().size() == 0) {
                    app.show(new GameOverController(app, game.getCurrentPlayer().getName()));
                }

                discardPile.getChildren().add(gameService.generateUICard((Card) evt.getNewValue(), false));
                String type = game.getDiscardCards().get(game.getDiscardCards().size() - 1).getType();
                if (CARD_TYPE.SKIP.toString().equals(type)) {
                    game.setCurrentPlayer(game.getCurrentPlayer().getNextPlayer().getNextPlayer());
                } else if (CARD_TYPE.REVERSE.toString().equals(type)) {
                    if (game.getPlayers().size() > 2) {
                        game.setClockwise(!game.isClockwise());
                        gameService.passTurn();
                    } else {
                        //TODO: bot skip doesnt work with 2 players (player + bot)
                        Player player = game.getCurrentPlayer();
                        game.setCurrentPlayer(null);
                        game.setCurrentPlayer(player);
                    }
                } else if (CARD_TYPE.WILD_DRAW_FOUR.toString().equals(type)) {
                    if (game.isClockwise()) {
                        game.getCurrentPlayer().getNextPlayer().setDebtCount(4);
                    } else {
                        game.getCurrentPlayer().getPreviousPlayer().setDebtCount(4);
                    }
                    gameService.passTurn();
                } else if (CARD_TYPE.DRAW_TWO.toString().equals(type)) {
                    if (game.isClockwise()) {
                        game.getCurrentPlayer().getNextPlayer().setDebtCount(2);
                    } else {
                        game.getCurrentPlayer().getPreviousPlayer().setDebtCount(2);
                    }
                    gameService.passTurn();
                } else {
                    gameService.passTurn();
                }
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
