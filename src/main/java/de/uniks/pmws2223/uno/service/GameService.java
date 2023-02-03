package de.uniks.pmws2223.uno.service;

import de.uniks.pmws2223.uno.Main;
import de.uniks.pmws2223.uno.model.Card;
import de.uniks.pmws2223.uno.model.Game;
import de.uniks.pmws2223.uno.model.Player;
import javafx.animation.Interpolator;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static de.uniks.pmws2223.uno.Constants.CARD_TYPE;
import static de.uniks.pmws2223.uno.Constants.UNO_DECK;

public class GameService {

    private final AnimationService animationService = new AnimationService();
    private Game game;
    private Player player;

    public List<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<Card>(UNO_DECK);
        Collections.shuffle(deck);
        return deck;
    }

    public Game generateGame(int botCount, String playerName) {
        game = new Game();
        List<Card> deck = generateDeck();
        game.withDrawCards(deck);

        //Generate Bots
        for (int _i = 0; _i < botCount; _i++) {
            game.withPlayers(new Player().setName("Bot0" + _i).setIsBot(true));
        }

        //Generate Player
        player = new Player().setName(playerName).setIsBot(false);
        game.withPlayers(player);

        //Set Order
        for (int _i = 0; _i < game.getPlayers().size(); _i++) {
            Player player = game.getPlayers().get(_i);
            player.setNextPlayer(game.getPlayers().get((_i + 1) % (game.getPlayers().size() - 1)));
        }

        return game;
    }

    public void dealStartingCards(Game game, StackPane drawPile, StackPane discardPile) {
        for (int _i = 0; _i < 7; _i++) {
            for (Player player : game.getPlayers()) {
                Card card = game.getDrawCards().get(0);
                player.withCards(card);
                game.withoutDrawCards(card);
                //player.withCards(deck.remove(0));
            }
        }
        //reveal first card
        Card card = game.getDrawCards().get(0);
        game.withDiscardCards(card);
        game.withoutDrawCards(card);
        discardPile.getChildren().add(generateUICard(card));

        //create initial draw stack
        for (int count = 0; count < game.getDrawCards().size(); count++) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/back.png"))));
            imageView.setFitWidth(64);
            imageView.setFitHeight(96);
            drawPile.getChildren().add(imageView);
            imageView.setOnMouseClicked(this::drawCard);
            //drawPile.getChildren().get(0).setLayoutX(count * 5);
            //drawPile.getChildren().get(0).setLayoutY(count * 5);
        }
    }

    private void drawCard(MouseEvent mouseEvent) {
        Card card = game.getDrawCards().get(0);
        player.withCards(card);
        game.withoutDrawCards(card);
    }

    public Pane generateUICard(Card card) {
        Pane UIcard = new Pane();
        UIcard.setUserData(card);
        Rectangle rec = new Rectangle();
        rec.setWidth(56);
        rec.setHeight(90);

        //Color color = Color.web(card.getColor(), 1);
        if (card.getColor() != null) {
            rec.setFill(Paint.valueOf(card.getColor()));
        } else {
            rec.setFill(Color.BLACK);
        }

        Label val = new Label();
        val.setText("" + card.getNumber());

        ImageView imageView = new ImageView();
        Image img = cardToImage(card);

        imageView.setImage(img);
        imageView.setFitWidth(64);
        imageView.setFitHeight(96);

        UIcard.getChildren().add(rec);
        UIcard.getChildren().get(0).setLayoutX(4);
        UIcard.getChildren().get(0).setLayoutY(5);
        //UIcard.getChildren().add(val);
        UIcard.getChildren().add(imageView);

        if (card.getOwner() != null && !card.getOwner().isIsBot()) {
            UIcard.setOnMouseEntered(this::onMouseEnterCard);
            UIcard.setOnMouseExited(this::onMouseExitCard);
            UIcard.setOnMouseClicked(this::playCard);
        }
        return UIcard;
    }

    private void playCard(MouseEvent mouseEvent) {
        Card card = (Card) ((Pane)mouseEvent.getSource()).getUserData();
        player.withoutCards(card);
        game.withDiscardCards(card);
    }

    private void onMouseExitCard(MouseEvent mouseEvent) {
        Pane pane = (Pane) mouseEvent.getSource();
        animationService.moveNode(pane.getChildren().get(0), 0, 0, 100, Interpolator.EASE_BOTH);
        animationService.moveNode(pane.getChildren().get(1), 0, 0, 100, Interpolator.EASE_BOTH);
        //pane.setViewOrder((int) pane.getUserData());
    }

    private void onMouseEnterCard(MouseEvent mouseEvent) {
        Pane pane = (Pane) mouseEvent.getSource();
        animationService.moveNode(pane.getChildren().get(0), 0, -20, 100, Interpolator.EASE_BOTH);
        animationService.moveNode(pane.getChildren().get(1), 0, -20, 100, Interpolator.EASE_BOTH);
        //pane.setViewOrder(-10);
    }

    private Image cardToImage(Card card) {
        Image img = null;
        if (card.getNumber() != -1) {
            //img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/" + card.getNumber() + ".png")));
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/" + card.getNumber() + ".png")));
        } else if (card.getType().equals(CARD_TYPE.DRAW_TWO.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/+2.png")));
        } else if (card.getType().equals(CARD_TYPE.SKIP.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/skip.png")));
        } else if (card.getType().equals(CARD_TYPE.REVERSE.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/reverse.png")));
        } else if (card.getType().equals(CARD_TYPE.WILD.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/wild.png")));
        } else if (card.getType().equals(CARD_TYPE.WILD_DRAW_FOUR.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/Cards/wilddrawfour.png")));
        }

        return img;
    }
}
