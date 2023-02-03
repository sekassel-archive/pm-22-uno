package de.uniks.pmws2223.uno.model;
import java.util.Objects;
import java.beans.PropertyChangeSupport;

public class Card
{
   public static final String PROPERTY_NUMBER = "number";
   public static final String PROPERTY_COLOR = "color";
   public static final String PROPERTY_TYPE = "type";
   public static final String PROPERTY_OWNER = "owner";
   private int number;
   private String color;
   private String type;
   private Player owner;
   protected PropertyChangeSupport listeners;

   public int getNumber()
   {
      return this.number;
   }

   public Card setNumber(int value)
   {
      if (value == this.number)
      {
         return this;
      }

      final int oldValue = this.number;
      this.number = value;
      this.firePropertyChange(PROPERTY_NUMBER, oldValue, value);
      return this;
   }

   public String getColor()
   {
      return this.color;
   }

   public Card setColor(String value)
   {
      if (Objects.equals(value, this.color))
      {
         return this;
      }

      final String oldValue = this.color;
      this.color = value;
      this.firePropertyChange(PROPERTY_COLOR, oldValue, value);
      return this;
   }

   public String getType()
   {
      return this.type;
   }

   public Card setType(String value)
   {
      if (Objects.equals(value, this.type))
      {
         return this;
      }

      final String oldValue = this.type;
      this.type = value;
      this.firePropertyChange(PROPERTY_TYPE, oldValue, value);
      return this;
   }

   public Player getOwner()
   {
      return this.owner;
   }

   public Card setOwner(Player value)
   {
      if (this.owner == value)
      {
         return this;
      }

      final Player oldValue = this.owner;
      if (this.owner != null)
      {
         this.owner = null;
         oldValue.withoutCards(this);
      }
      this.owner = value;
      if (value != null)
      {
         value.withCards(this);
      }
      this.firePropertyChange(PROPERTY_OWNER, oldValue, value);
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

   @Override
   public String toString()
   {
      final StringBuilder result = new StringBuilder();
      result.append(' ').append(this.getColor());
      result.append(' ').append(this.getType());
      return result.substring(1);
   }

   public void removeYou()
   {
      this.setOwner(null);
   }
}
