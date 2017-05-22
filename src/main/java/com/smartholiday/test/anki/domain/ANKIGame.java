package com.smartholiday.test.anki.domain;

import java.util.HashMap;
import java.util.Map;

import static com.smartholiday.test.anki.domain.StudentFeedback.I_SHOULD_IMPROVE_THE_ANSWER;
import static com.smartholiday.test.anki.domain.StudentFeedback.I_IGNORED_THE_ANSWER;
import static com.smartholiday.test.anki.domain.StudentFeedback.I_KNEW_THE_ANSWER;

public class ANKIGame
{


  Map<StudentFeedback,Box> boxesForAnswers = new HashMap(3);

  private final DeckRepository deckRepository;
  private Deck deck;
  private Card currentCard;


  public ANKIGame(DeckRepository deckLoader)
  {
    this(deckLoader,
         new Box("GREEN"),
         new Box("ORANGE"),
         new Box("RED")
         );
  }

  public ANKIGame(DeckRepository deckLoader,
                  Box knownAnswer,
                  Box answerToImprove,
                  Box unknownAnswer)
  {
    this.deckRepository = deckLoader;
    this.boxesForAnswers.put(I_KNEW_THE_ANSWER, knownAnswer);
    this.boxesForAnswers.put(I_SHOULD_IMPROVE_THE_ANSWER, answerToImprove);
    this.boxesForAnswers.put(I_IGNORED_THE_ANSWER, unknownAnswer);
  }

  public boolean hasNextCard()
  {
    boolean deckHasNextCards = deck.hasNext();
    if (!deckHasNextCards) {
      Box boxWithUnknownCards = getRedBox();
      if (!boxWithUnknownCards.isEmpty())
      {
        deck = new DeckList(boxWithUnknownCards.removeAllCards());
        return true;
      }
    }
    return deckHasNextCards;
  }

  public String getCardQuestion() {
    if(currentCard==null) {
      return "No more cards.";
    }
    return currentCard.getQuestion();
  }

  public void loadNextCard()
  {
    currentCard = null;
    if(deck.hasNext()) {
      currentCard = deck.next();
    }
  }

  public String getCardAnswer() {
    if(currentCard==null) {
      return "No more cards.";
    }
    return currentCard.getAnswer();
  }

  public void closeSession() {
    deckRepository.saveDeckOfTomorrow(getOrangeBox().removeAllCards());
    deckRepository.saveDeckOfTheDayAfterTomorrow(getGreenBox().removeAllCards());
  }

  public void openSession() {
    this.deck = deckRepository.loadDeck();
  }

  public void placeCard(StudentFeedback feedback)
  {
    Box box = boxesForAnswers.get(feedback);
    System.out.println("Moving card to box ["+box.getColour()+"].");
    box.addCard(currentCard);
  }

  public String getClosingMessage()
  {
    if(getOrangeBox().isEmpty()) {
      return "Congratulations! You knew every question.";
    }
    return "Goodbye, see you tomorrow to improve your skills.";
  }

  private Box getGreenBox() {
    return boxesForAnswers.get(I_KNEW_THE_ANSWER);
  }

  private Box getRedBox() {
    return boxesForAnswers.get(I_IGNORED_THE_ANSWER);
  }

  private Box getOrangeBox() {
    return boxesForAnswers.get(I_SHOULD_IMPROVE_THE_ANSWER);
  }


}
