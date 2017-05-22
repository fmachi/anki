package com.smartholiday.test.anki.domain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ANKIGameTest
{
  Card card1 = new Card(
      "Which famous film star did Pop artist Andy Warhol make more portraits of than any other?",
      "Marilyn Monroe");
  Card card2 = new Card(
      "The Mona Lisa, Leonardo da Vinci’s magnum opus, draws crowds into which famous European museum?",
      "The Musée du Louvre, Paris");
  Card card3 = new Card("With which colour is the great French painter Yves Klein often associated?",
                        "Blue");
  Card card4 = new Card("Which Pop artist famously based so much of his art on blown-up comic strips?",
                        "Roy Lichtenstein");
  Card card5 = new Card(
      "Which Spanish artist, whose first name was Pablo, created the 7.8 metre wide painting Guernica in 1937?",
      "Picasso");
  Card card6 = new Card("Which eccentric Dutch artist is known for chopping off part of his ear?",
                        "Vincent van Gogh");
  Card card7 = new Card(
      "The art critic Louis Leroy coined the term Impressionism from a painting titled Impression, soleil levant. But which artist created it?",
      "Claude Monet");
  List<Card> cardList = asList(
      card1,
      card2,
      card3,
      card4,
      card5,
      card6,
      card7
  );

  @Captor
  ArgumentCaptor<List<Card>> listCaptor;

  @Mock
  private DeckRepository deckRepository;

  @Before
  public void setup()
  {

    when(deckRepository.loadDeck()).thenReturn(
        new DeckList(
            cardList
        ));
  }

  @Test
  public void happyPathAllKnown()
  {
    ANKIGame game = new ANKIGame(deckRepository);

    game.openSession();

    verify(deckRepository).loadDeck();

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The Mona Lisa, Leonardo da Vinci’s magnum opus, draws crowds into which famous European museum?")));
    assertThat(game.getCardAnswer(),is(equalTo("The Musée du Louvre, Paris")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The art critic Louis Leroy coined the term Impressionism from a painting titled Impression, soleil levant. But which artist created it?")));
    assertThat(game.getCardAnswer(),is(equalTo("Claude Monet")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which Pop artist famously based so much of his art on blown-up comic strips?")));
    assertThat(game.getCardAnswer(),is(equalTo("Roy Lichtenstein")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which Spanish artist, whose first name was Pablo, created the 7.8 metre wide painting Guernica in 1937?")));
    assertThat(game.getCardAnswer(),is(equalTo("Picasso")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which eccentric Dutch artist is known for chopping off part of his ear?")));
    assertThat(game.getCardAnswer(),is(equalTo("Vincent van Gogh")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which famous film star did Pop artist Andy Warhol make more portraits of than any other?")));
    assertThat(game.getCardAnswer(),is(equalTo("Marilyn Monroe")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("With which colour is the great French painter Yves Klein often associated?")));
    assertThat(game.getCardAnswer(),is(equalTo("Blue")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(false));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("No more cards.")));
    assertThat(game.getCardAnswer(),is(equalTo("No more cards.")));

    game.closeSession();

    verify(deckRepository).saveDeckOfTomorrow(listCaptor.capture());
    verify(deckRepository).saveDeckOfTheDayAfterTomorrow(listCaptor.capture());

    List<List<Card>> allValues = listCaptor.getAllValues();
    assertThat(allValues.size(),is(equalTo(2)));

    List<Card> cardsOfTomorrow = allValues.get(0);
    assertThat(cardsOfTomorrow.size(),is(equalTo(0)));

    List<Card> cardsOfDayAfterTomorrow = allValues.get(1);
    assertThat(cardsOfDayAfterTomorrow.size(),is(equalTo(7)));

    checkCardsAre(cardsOfDayAfterTomorrow,card1,card2,card3,card4,card5,card6,card7);
  }


  @Test
  public void mixedScenario()
  {
    ANKIGame game = new ANKIGame(deckRepository);

    game.openSession();

    verify(deckRepository).loadDeck();

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The Mona Lisa, Leonardo da Vinci’s magnum opus, draws crowds into which famous European museum?")));
    assertThat(game.getCardAnswer(),is(equalTo("The Musée du Louvre, Paris")));
    game.placeCard(StudentFeedback.I_IGNORED_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The art critic Louis Leroy coined the term Impressionism from a painting titled Impression, soleil levant. But which artist created it?")));
    assertThat(game.getCardAnswer(),is(equalTo("Claude Monet")));
    game.placeCard(StudentFeedback.I_IGNORED_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which Pop artist famously based so much of his art on blown-up comic strips?")));
    assertThat(game.getCardAnswer(),is(equalTo("Roy Lichtenstein")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which Spanish artist, whose first name was Pablo, created the 7.8 metre wide painting Guernica in 1937?")));
    assertThat(game.getCardAnswer(),is(equalTo("Picasso")));
    game.placeCard(StudentFeedback.I_SHOULD_IMPROVE_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which eccentric Dutch artist is known for chopping off part of his ear?")));
    assertThat(game.getCardAnswer(),is(equalTo("Vincent van Gogh")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which famous film star did Pop artist Andy Warhol make more portraits of than any other?")));
    assertThat(game.getCardAnswer(),is(equalTo("Marilyn Monroe")));
    game.placeCard(StudentFeedback.I_IGNORED_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("With which colour is the great French painter Yves Klein often associated?")));
    assertThat(game.getCardAnswer(),is(equalTo("Blue")));
    game.placeCard(StudentFeedback.I_SHOULD_IMPROVE_THE_ANSWER);

    // Second part for the session (Ignored first time)

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The Mona Lisa, Leonardo da Vinci’s magnum opus, draws crowds into which famous European museum?")));
    assertThat(game.getCardAnswer(),is(equalTo("The Musée du Louvre, Paris")));
    game.placeCard(StudentFeedback.I_IGNORED_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The art critic Louis Leroy coined the term Impressionism from a painting titled Impression, soleil levant. But which artist created it?")));
    assertThat(game.getCardAnswer(),is(equalTo("Claude Monet")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("Which famous film star did Pop artist Andy Warhol make more portraits of than any other?")));
    assertThat(game.getCardAnswer(),is(equalTo("Marilyn Monroe")));
    game.placeCard(StudentFeedback.I_SHOULD_IMPROVE_THE_ANSWER);


    // Second part for the session (Ignored second time)

    assertThat(game.hasNextCard(),is(true));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("The Mona Lisa, Leonardo da Vinci’s magnum opus, draws crowds into which famous European museum?")));
    assertThat(game.getCardAnswer(),is(equalTo("The Musée du Louvre, Paris")));
    game.placeCard(StudentFeedback.I_KNEW_THE_ANSWER);

    assertThat(game.hasNextCard(),is(false));
    game.loadNextCard();
    assertThat(game.getCardQuestion(),is(equalTo("No more cards.")));
    assertThat(game.getCardAnswer(),is(equalTo("No more cards.")));

    game.closeSession();

    verify(deckRepository).saveDeckOfTomorrow(listCaptor.capture());
    verify(deckRepository).saveDeckOfTheDayAfterTomorrow(listCaptor.capture());

    List<List<Card>> allValues = listCaptor.getAllValues();
    assertThat(allValues.size(),is(equalTo(2)));

    List<Card> cardsOfTomorrow = allValues.get(0);
    assertThat(cardsOfTomorrow.size(),is(equalTo(3)));

    List<Card> cardsOfDayAfterTomorrow = allValues.get(1);
    assertThat(cardsOfDayAfterTomorrow.size(),is(equalTo(4)));

    checkCardsAre(cardsOfTomorrow,card1,card3,card5);
    checkCardsAre(cardsOfDayAfterTomorrow,card4,card2,card6,card7);
  }



  private void checkCardsAre(List<Card> list,
                             Card...cards)
  {
    Set<Card> toMatch = new HashSet(list);
    for(Card card:cards) {
      assertThat("Matching "+card,toMatch.contains(card),is(true));

    }
  }

}
