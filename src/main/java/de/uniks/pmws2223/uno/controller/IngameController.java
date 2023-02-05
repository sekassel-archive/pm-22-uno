package de.uniks.pmws2223.uno.controller;

import de.uniks.pmws2223.uno.App;
import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import de.uniks.pmws2223.uno.service.AnimationService;
import de.uniks.pmws2223.uno.service.BotService;
import de.uniks.pmws2223.uno.service.GameService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;

import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static de.uniks.pmws2223.uno.Constants.CARD_TYPE;
import static de.uniks.pmws2223.uno.Constants.INGAME_TITLE;

public class IngameController implements Controller {

    private final App app;
    private final Game game;
    private final BotService botService;
    private final AnimationService animationService;
    private final GameService gameService;
    private List<Card> deck;
    private final List<PlayerController> playerControllers = new ArrayList<>();
    private PropertyChangeListener discardListener;
    private PropertyChangeListener drawListener;
    private Pane wishColorParent;

    public IngameController(App app, Game game, GameService gameService, AnimationService animationService) {
        this.app = app;
        this.game = game;
        this.gameService = gameService;
        this.botService = new BotService(gameService);
        this.animationService = animationService;
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
        for (Node colorRec : wishColorParent.getChildren()) {
            colorRec.setOnMouseClicked(this::wishColor);
        }

        for (Player player : game.getPlayers()) {
            PlayerController playerController = new PlayerController(player, gameService, player.isIsBot() ? null : wishColorParent, botService, animationService);
            playerController.init();
            playerControllers.add(playerController);

            if (player.isIsBot()) {
                HBox root = (HBox) parent.lookup("#botBox");
                root.getChildren().add(playerController.render());
            } else {
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

                Pane newCard = gameService.generateUICard((Card) evt.getNewValue(), false);
                discardPile.getChildren().add(newCard);
                newCard.setTranslateY((float) -(discardPile.getChildren().size() - 1) / 4);

                String type = game.getDiscardCards().get(game.getDiscardCards().size() - 1).getType();
                if (CARD_TYPE.SKIP.toString().equals(type)) {
                    Player oldPlayer = game.getCurrentPlayer();
                    game.setCurrentPlayer(null);
                    if (game.isClockwise()) {
                        game.setCurrentPlayer(oldPlayer.getNextPlayer().getNextPlayer());
                    } else {
                        game.setCurrentPlayer(oldPlayer.getPreviousPlayer().getPreviousPlayer());
                    }
                } else if (CARD_TYPE.REVERSE.toString().equals(type)) {
                    game.setClockwise(!game.isClockwise());
                    gameService.passTurn();
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
            } else if (evt.getOldValue() != null) {
                discardPile.getChildren().removeIf(card -> card.getUserData().equals(evt.getOldValue()));
                for (int count = 0; count < discardPile.getChildren().size(); count++) {
                    discardPile.getChildren().get(discardPile.getChildren().size() - 1).setTranslateY(-((float) count++ / 4));
                }
            }
        };

        drawListener = event -> {
            if (event.getNewValue() != null) {
                ImageView imageView = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/back.png"))));
                imageView.setFitWidth(64);
                imageView.setFitHeight(96);
                drawPile.getChildren().add(imageView);
                imageView.setOnMouseClicked(gameService::drawClickCard);
                //drawPile.getChildren().get(drawPile.getChildren().size()-1).setTranslateX((count / 16));
                drawPile.getChildren().get(drawPile.getChildren().size() - 1).setTranslateY(-((float) game.getDrawCards().size() / 8));
            } else if (event.getOldValue() != null) {
                drawPile.getChildren().remove(game.getDrawCards().size());
                if (game.getDrawCards().size() == 0) {
                    List<Card> newDeck = new ArrayList<>();//game.getDiscardCards().subList(0, game.getDiscardCards().size() - 2);
                    while (game.getDiscardCards().size() >= 2) {
                        if (game.getDiscardCards().get(0).getType().equals(CARD_TYPE.WILD.toString()) || game.getDiscardCards().get(0).getType().equals(CARD_TYPE.WILD_DRAW_FOUR.toString())) {
                            game.getDiscardCards().get(0).setColor(null);
                        }
                        newDeck.add(game.getDiscardCards().get(0));
                        game.withoutDiscardCards(game.getDiscardCards().get(0));
                    }
                    Collections.shuffle(newDeck);
                    game.withDrawCards(newDeck);
                }
            }
        };

        game.listeners().addPropertyChangeListener(Game.PROPERTY_DISCARD_CARDS, discardListener);
        game.listeners().addPropertyChangeListener(Game.PROPERTY_DRAW_CARDS, drawListener);

        return parent;
    }

    private void wishColor(MouseEvent mouseEvent) {
        Rectangle rec = (Rectangle) mouseEvent.getSource();
        for (Node recs : wishColorParent.getChildren()) {
            ((Rectangle) recs).setStrokeWidth(1);
        }
        rec.setStrokeWidth(4);
        for (Player player : game.getPlayers()) {
            player.setWishedColor(rec.getFill().toString());
        }
    }

    @Override
    public void destroy() {
        game.listeners().removePropertyChangeListener(Game.PROPERTY_DISCARD_CARDS, discardListener);
        game.listeners().removePropertyChangeListener(Game.PROPERTY_DRAW_CARDS, drawListener);

        for (PlayerController playerController : playerControllers) {
            playerController.destroy();
        }
    }

}
