package de.uniks.pmws2223.uno.service;

import static de.uniks.pmws2223.uno.Constants.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import de.uniks.pmws2223.uno.model.Card;

public class GameService {

    public List<Card> generateDeck(){
        ArrayList<Card> deck = new ArrayList<Card>(UNO_DECK);
        Collections.shuffle(deck);
        return deck;
    }
    
}
