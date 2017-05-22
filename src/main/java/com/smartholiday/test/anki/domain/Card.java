package com.smartholiday.test.anki.domain;

public class Card implements Comparable<Card>
{
  private final String question;
  private final String answer;

  public Card(String question, String answer)
  {
    this.question = question;
    this.answer = answer;
  }

  public String getQuestion()
  {
    return question;
  }

  public String getAnswer()
  {
    return answer;
  }

  @Override public String toString()
  {
    return "Card{" +
        "question='" + question + '\'' +
        ", answer='" + answer + '\'' +
        '}';
  }

  @Override public boolean equals(Object o)
  {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;

    Card card = (Card) o;

    if (question != null ? !question.equals(card.question) : card.question != null)
      return false;
    return answer != null ? answer.equals(card.answer) : card.answer == null;
  }

  @Override public int hashCode()
  {
    int result = question != null ? question.hashCode() : 0;
    result = 31 * result + (answer != null ? answer.hashCode() : 0);
    return result;
  }

  public int compareTo(Card o)
  {
    return this.question.compareTo(o.getQuestion());
  }
}
