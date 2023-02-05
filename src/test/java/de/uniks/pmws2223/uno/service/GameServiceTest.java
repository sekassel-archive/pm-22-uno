package de.uniks.pmws2223.uno.service;

import org.junit.Test;

public class GameServiceTest {
    @Test
    private void playCardNormal() {
        //setup work
        //play card
        //assert card on discardpile
        //assert card not in hand
    }

    @Test
    private void playCardSkip() {
        //setup work
        //play card
        //assert card on discardpile
        //assert card not in hand
        //assert next player was skipped
    }

    @Test
    private void playCardReverse() {
        //setup work
        //play card
        //assert card on discardpile
        //assert card not in hand
        //assert next player is previous player
    }

    @Test
    private void playCardWild() {
        //setup work
        //play card and wish color
        //assert card on discardpile
        //assert card not in hand
        //assert top card on discardpile has wished color
    }

    @Test
    private void playCardWildDrawFour() {
        //setup work
        //play card and wish color
        //assert card on discardpile
        //assert card not in hand
        //assert top card on discardpile has wished color
        //assert next player has 4 more cards in his hand
        //assert next player is skipped
    }

    @Test
    private void playCardDrawTwo() {
        //setup work
        //play card
        //assert card on discardpile
        //assert card not in hand
        //next player has 2 more cards in his hand
        //assert next player is skipped
    }

    @Test
    private void generateDeck() {
        //setup work
        //assert that deck contains the correct cards
    }

    @Test
    private void cardIsPlayable() {
        //setup work
        //assert if playable card is playable
        //assert if not playable card is not playable
    }

    @Test
    private void generateGame() {
        //setup work
        //assert that game is generated correctly
    }

}
