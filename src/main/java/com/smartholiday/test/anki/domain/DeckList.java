package com.smartholiday.test.anki.domain;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import static java.util.Collections.sort;

public class DeckList implements Deck
{
  private final List<Card> cards;
  private final Iterator<Card> iterator;

  public DeckList(List<Card> cards)
  {
    this.cards = new LinkedList<Card>(cards);
    sort(this.cards);
    this.iterator = this.cards.iterator();
  }

  public boolean hasNext()
  {
    return iterator.hasNext();
  }

  public Card next()
  {
    return iterator.next();
  }

  public void remove()
  {
    throw new UnsupportedOperationException("Method not allowed");
  }

  @Override public String toString()
  {
    return "DeckList{" +
        "cards=" + cards +
        '}';
  }
}
