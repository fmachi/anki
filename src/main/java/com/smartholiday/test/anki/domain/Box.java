package com.smartholiday.test.anki.domain;

import java.util.LinkedList;
import java.util.List;

public class Box
{
  private final String colour;
  private final List<Card> cards = new LinkedList();

  public Box(String colour)
  {
    this.colour = colour;
  }

  public void addCard(Card card) {
    cards.add(card);
  }

  public List<Card> removeAllCards()
  {
    LinkedList<Card> cards = new LinkedList<Card>(this.cards);
    this.cards.clear();
    return cards;
  }

  public boolean isEmpty()
  {
    return cards.isEmpty();
  }

  public String getColour()
  {
    return colour;
  }

  @Override public String toString()
  {
    return "Box{" +
        "colour='" + colour + '\'' +
        ", cards=" + cards +
        '}';
  }
}
