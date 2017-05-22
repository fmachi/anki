package com.smartholiday.test.anki;

import com.smartholiday.test.anki.adapters.CSVFileDeckRepository;
import com.smartholiday.test.anki.domain.*;

import java.util.Collection;
import java.util.Scanner;

import static java.util.Arrays.asList;

public final class Main
{

  public static void main(String[] args)
  {
    ANKIGame game = buildGame();

    game.openSession();

    while (game.hasNextCard())
    {
      game.loadNextCard();

      showQuestion(game.getCardQuestion());

      waitForStudentToThink();

      showAnswer(game.getCardAnswer());

      StudentFeedback feedback = takeFeedback();

      game.placeCard(feedback);

    }

    show("END: ",game.getClosingMessage());

    game.closeSession();

  }

  private static StudentFeedback takeFeedback()
  {
    System.out.println();
    System.out.println("Do you know the answer? Press the number, then enter for (default is No):");
    System.out.println("1) Yes");
    System.out.println("2) So and so");
    System.out.println("3) No");

    int value = getScanner().nextInt();
    StudentFeedback feedback = StudentFeedback.I_IGNORED_THE_ANSWER;
    if(value==1) {
      feedback = StudentFeedback.I_KNEW_THE_ANSWER;
    } else if(value==2) {
      feedback = StudentFeedback.I_SHOULD_IMPROVE_THE_ANSWER;
    }
    show("Your answer is:",feedback.name());
    return feedback;
  }

  private static void waitForStudentToThink()
  {
    System.out.println("Press enter when you are ready to check the answer!");
    getScanner().nextLine();

  }

  private static Scanner getScanner() {
    return new Scanner(System.in);
  }
  private static void showQuestion(String question) {
    System.out.println();
    show("Next question is: ",question);
  }

  private static void showAnswer(String answer) {
    show("The answer is: ",answer);
  }

  private static void show(String prefix, String what)
  {
    System.out.println(prefix + what);
  }

  private static ANKIGame buildGame()
  {
    return new ANKIGame(buildDeckLoader());
  }

  private static DeckRepository buildDeckLoader()
  {
    return new CSVFileDeckRepository("deck.csv","UTF-8");
  }

  private static DeckRepository buildFakeDeckLoader()
  {
    return new DeckRepository()
    {
      public Deck loadDeck()
      {
        return new DeckList(asList(
            new Card("First Question", "answer1"),
            new Card("Second Question", "answer2"),
            new Card("Third Question", "answer3"),
            new Card("Fourth Question", "answer4"),
            new Card("Fifth Question", "answer5")
        ));
      }

      public void saveDeckOfTomorrow(Collection<Card> cards)
      {

      }

      public void saveDeckOfTheDayAfterTomorrow(Collection<Card> cards)
      {

      }
    };
  }

}
