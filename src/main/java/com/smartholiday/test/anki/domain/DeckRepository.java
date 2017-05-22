package com.smartholiday.test.anki.domain;

import java.util.Collection;

public interface DeckRepository
{
  public Deck loadDeck();

  public void saveDeckOfTomorrow(Collection<Card> cards);

  public void saveDeckOfTheDayAfterTomorrow(Collection<Card> cards);
}
