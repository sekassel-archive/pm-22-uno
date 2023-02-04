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
import javafx.scene.layout.HBox;
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

    private final AnimationService animationService;
    private Game game;
    private Player player;

    public GameService(AnimationService animationService) {
        this.animationService = animationService;
    }

    public List<Card> generateDeck() {
        ArrayList<Card> deck = new ArrayList<Card>(UNO_DECK);
        Collections.shuffle(deck);
        return deck;
    }

    public Game generateGame(int botCount, String playerName) {
        game = new Game();
        game.setClockwise(true);
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
            Player _player = game.getPlayers().get(_i);
            _player.setNextPlayer(game.getPlayers().get((_i + 1) % (game.getPlayers().size())));
        }

        game.setCurrentPlayer(player);
        return game;
    }

    public void dealStartingCards(Game game, StackPane drawPile, StackPane discardPile) {
        for (int _i = 0; _i < 7; _i++) {
            for (Player _player : game.getPlayers()) {
                Card card = game.getDrawCards().get(0);
                _player.withCards(card);
                game.withoutDrawCards(card);
                //player.withCards(deck.remove(0));
            }
        }
        //reveal first card
        Card card = game.getDrawCards().get(0);
        game.withDiscardCards(card);
        game.withoutDrawCards(card);
        discardPile.getChildren().add(generateUICard(card, false));

        //create initial draw stack
        for (int count = 0; count < game.getDrawCards().size(); count++) {
            ImageView imageView = new ImageView(new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/back.png"))));
            imageView.setFitWidth(64);
            imageView.setFitHeight(96);
            drawPile.getChildren().add(imageView);
            imageView.setOnMouseClicked(this::drawClickCard);
            //drawPile.getChildren().get(drawPile.getChildren().size()-1).setTranslateX((count / 16));
            drawPile.getChildren().get(drawPile.getChildren().size()-1).setTranslateY(-((float)count / 8));
        }
    }

    public void drawClickCard(MouseEvent mouseEvent) {
        if (player.getCurrentGame() != null) {
            drawCard(true);
        }
    }

    public void drawCard(boolean skip) {
        Card card = game.getDrawCards().get(0);
        game.getCurrentPlayer().withCards(card);
        game.withoutDrawCards(card);
        if (skip) {
            if (!cardIsPlayable(card, game.getDiscardCards().get(game.getDiscardCards().size() - 1))) {
                passTurn();
            } else {
                Card _card = game.getCurrentPlayer().getCards().get(game.getCurrentPlayer().getCards().size() - 1);
                playCard(_card);
            }
        }
    }

    public Pane generateUICard(Card card, boolean hidden) {
        Pane UIcard = new Pane();
        UIcard.setUserData(card);
        Rectangle rec = new Rectangle();
        rec.setWidth(58);
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
        hidden = false;
        Image img = !hidden ? cardToImage(card) : new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/back.png")));

        imageView.setImage(img);
        imageView.setFitWidth(64);
        imageView.setFitHeight(96);

        UIcard.getChildren().add(rec);
        UIcard.getChildren().get(0).setLayoutX(3);
        UIcard.getChildren().get(0).setLayoutY(3);
        //UIcard.getChildren().add(val);
        UIcard.getChildren().add(imageView);

        if (card.getOwner() != null && !card.getOwner().isIsBot()) {
            UIcard.setOnMouseEntered(this::onMouseEnterCard);
            UIcard.setOnMouseExited(this::onMouseExitCard);
            UIcard.setOnMouseClicked(this::playClickedCard);
        }
        return UIcard;
    }

    public void playClickedCard(MouseEvent mouseEvent) {
        if (player.getCurrentGame() != null) {
            Card card;
            if (mouseEvent != null) {
                if (player.getDebtCount() == 0) {
                    card = (Card) ((Pane) mouseEvent.getSource()).getUserData();
                } else {
                    Card _card = (Card) ((Pane) mouseEvent.getSource()).getUserData();
                    if (player.getDebtCount() == 2 && (_card.getType().equals(CARD_TYPE.DRAW_TWO.toString()))) {
                        card = _card;
                        player.setDebtCount(0);
                    } else if (player.getDebtCount() == 4 && (_card.getType().equals(CARD_TYPE.WILD_DRAW_FOUR.toString()))) {
                        card = _card;
                        player.setDebtCount(0);
                    } else return;
                }
            } else {
                card = game.getCurrentPlayer().getCards().get(game.getCurrentPlayer().getCards().size() - 1);
            }
            playCard(card);
        }
    }

    public void playCard(Card card){
        Card discardPileTopCard = game.getDiscardCards().get(game.getDiscardCards().size()-1);

        if(cardIsPlayable(card, discardPileTopCard)){
            game.getCurrentPlayer().withoutCards(card);
            game.withDiscardCards(card);
        }
    }

    public boolean cardIsPlayable(Card cardToPlay, Card discardTopCard){
        String discardPileTopCardType = discardTopCard.getType();
        boolean wildCardFirstCard = game.getDiscardCards().size() == 1 && (discardPileTopCardType.equals(CARD_TYPE.WILD.toString()) || discardPileTopCardType.equals(CARD_TYPE.WILD_DRAW_FOUR.toString()));
        String cardType = cardToPlay.getType();
        boolean isWildCard = (cardType.equals(CARD_TYPE.WILD.toString()) || cardType.equals(CARD_TYPE.WILD_DRAW_FOUR.toString()));

        if(isWildCard){
            if(cardToPlay.getOwner().getWishedColor() != null){
                cardToPlay.setColor(cardToPlay.getOwner().getWishedColor());
                return true;
            }
            return false;
        }
        else return wildCardFirstCard || cardToPlay.getColor().equals(discardTopCard.getColor()) || (cardToPlay.getNumber() != -1 && (cardToPlay.getNumber() == discardTopCard.getNumber())) ||
        (!cardType.equals(CARD_TYPE.NORMAL.toString()) && cardType.equals(discardTopCard.getType()));
    }

    public void passTurn(){
        if (game.isClockwise()) {
            game.setCurrentPlayer(game.getCurrentPlayer().getNextPlayer());
        } else {
            game.setCurrentPlayer(game.getCurrentPlayer().getPreviousPlayer());
        }
    }

    private void onMouseExitCard(MouseEvent mouseEvent) {
        Pane pane = (Pane) mouseEvent.getSource();
        animationService.moveNode(pane.getChildren().get(0), 0, 0, 100, Interpolator.EASE_BOTH);
        animationService.moveNode(pane.getChildren().get(1), 0, 0, 100, Interpolator.EASE_BOTH);
        //pane.setViewOrder(-((HBox) pane.getParent()).getChildren().indexOf(pane));
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
            //TODO: Das lieber zu nem switch statement
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/" + card.getNumber() + ".png")));
        } else if (card.getType().equals(CARD_TYPE.DRAW_TWO.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/+2.png")));
        } else if (card.getType().equals(CARD_TYPE.SKIP.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/skip.png")));
        } else if (card.getType().equals(CARD_TYPE.REVERSE.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/reverse.png")));
        } else if (card.getType().equals(CARD_TYPE.WILD.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/wild.png")));
        } else if (card.getType().equals(CARD_TYPE.WILD_DRAW_FOUR.toString())) {
            img = new Image(Objects.requireNonNull(Main.class.getResourceAsStream("image/cards/wilddrawfour.png")));
        }

        return img;
    }
}
