package de.uniks.pmws2223.uno.model;
import java.util.ArrayList;
import java.util.List;
import java.util.Collections;
import java.util.Collection;
import java.beans.PropertyChangeSupport;

public class Game
{
   public static final String PROPERTY_BOTS = "bots";
   public static final String PROPERTY_HUMAN = "human";
   public static final String PROPERTY_DISCARD_CARDS = "discardCards";
   public static final String PROPERTY_DRAW_CARDS = "drawCards";
   private List<Player> bots;
   private Player human;
   private List<Card> discardCards;
   private List<Card> drawCards;
   protected PropertyChangeSupport listeners;

   public List<Player> getBots()
   {
      return this.bots != null ? Collections.unmodifiableList(this.bots) : Collections.emptyList();
   }

   public Game withBots(Player value)
   {
      if (this.bots == null)
      {
         this.bots = new ArrayList<>();
      }
      if (!this.bots.contains(value))
      {
         this.bots.add(value);
         this.firePropertyChange(PROPERTY_BOTS, null, value);
      }
      return this;
   }

   public Game withBots(Player... value)
   {
      for (final Player item : value)
      {
         this.withBots(item);
      }
      return this;
   }

   public Game withBots(Collection<? extends Player> value)
   {
      for (final Player item : value)
      {
         this.withBots(item);
      }
      return this;
   }

   public Game withoutBots(Player value)
   {
      if (this.bots != null && this.bots.remove(value))
      {
         this.firePropertyChange(PROPERTY_BOTS, value, null);
      }
      return this;
   }

   public Game withoutBots(Player... value)
   {
      for (final Player item : value)
      {
         this.withoutBots(item);
      }
      return this;
   }

   public Game withoutBots(Collection<? extends Player> value)
   {
      for (final Player item : value)
      {
         this.withoutBots(item);
      }
      return this;
   }

   public Player getHuman()
   {
      return this.human;
   }

   public Game setHuman(Player value)
   {
      if (this.human == value)
      {
         return this;
      }

      final Player oldValue = this.human;
      this.human = value;
      this.firePropertyChange(PROPERTY_HUMAN, oldValue, value);
      return this;
   }

   public List<Card> getDiscardCards()
   {
      return this.discardCards != null ? Collections.unmodifiableList(this.discardCards) : Collections.emptyList();
   }

   public Game withDiscardCards(Card value)
   {
      if (this.discardCards == null)
      {
         this.discardCards = new ArrayList<>();
      }
      if (!this.discardCards.contains(value))
      {
         this.discardCards.add(value);
         this.firePropertyChange(PROPERTY_DISCARD_CARDS, null, value);
      }
      return this;
   }

   public Game withDiscardCards(Card... value)
   {
      for (final Card item : value)
      {
         this.withDiscardCards(item);
      }
      return this;
   }

   public Game withDiscardCards(Collection<? extends Card> value)
   {
      for (final Card item : value)
      {
         this.withDiscardCards(item);
      }
      return this;
   }

   public Game withoutDiscardCards(Card value)
   {
      if (this.discardCards != null && this.discardCards.remove(value))
      {
         this.firePropertyChange(PROPERTY_DISCARD_CARDS, value, null);
      }
      return this;
   }

   public Game withoutDiscardCards(Card... value)
   {
      for (final Card item : value)
      {
         this.withoutDiscardCards(item);
      }
      return this;
   }

   public Game withoutDiscardCards(Collection<? extends Card> value)
   {
      for (final Card item : value)
      {
         this.withoutDiscardCards(item);
      }
      return this;
   }

   public List<Card> getDrawCards()
   {
      return this.drawCards != null ? Collections.unmodifiableList(this.drawCards) : Collections.emptyList();
   }

   public Game withDrawCards(Card value)
   {
      if (this.drawCards == null)
      {
         this.drawCards = new ArrayList<>();
      }
      if (!this.drawCards.contains(value))
      {
         this.drawCards.add(value);
         this.firePropertyChange(PROPERTY_DRAW_CARDS, null, value);
      }
      return this;
   }

   public Game withDrawCards(Card... value)
   {
      for (final Card item : value)
      {
         this.withDrawCards(item);
      }
      return this;
   }

   public Game withDrawCards(Collection<? extends Card> value)
   {
      for (final Card item : value)
      {
         this.withDrawCards(item);
      }
      return this;
   }

   public Game withoutDrawCards(Card value)
   {
      if (this.drawCards != null && this.drawCards.remove(value))
      {
         this.firePropertyChange(PROPERTY_DRAW_CARDS, value, null);
      }
      return this;
   }

   public Game withoutDrawCards(Card... value)
   {
      for (final Card item : value)
      {
         this.withoutDrawCards(item);
      }
      return this;
   }

   public Game withoutDrawCards(Collection<? extends Card> value)
   {
      for (final Card item : value)
      {
         this.withoutDrawCards(item);
      }
      return this;
   }

   public boolean firePropertyChange(String propertyName, Object oldValue, Object newValue)
   {
      if (this.listeners != null)
      {
         this.listeners.firePropertyChange(propertyName, oldValue, newValue);
         return true;
      }
      return false;
   }

   public PropertyChangeSupport listeners()
   {
      if (this.listeners == null)
      {
         this.listeners = new PropertyChangeSupport(this);
      }
      return this.listeners;
   }

   public void removeYou()
   {
      this.withoutBots(new ArrayList<>(this.getBots()));
      this.setHuman(null);
      this.withoutDiscardCards(new ArrayList<>(this.getDiscardCards()));
      this.withoutDrawCards(new ArrayList<>(this.getDrawCards()));
   }
}
