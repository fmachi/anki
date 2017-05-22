package com.smartholiday.test.anki.adapters;

import com.smartholiday.test.anki.domain.Card;
import com.smartholiday.test.anki.domain.Deck;
import com.smartholiday.test.anki.domain.DeckList;
import com.smartholiday.test.anki.domain.DeckRepository;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

public class CSVFileDeckRepository implements DeckRepository
{
  private final String fileName;
  private final String encoding;

  public CSVFileDeckRepository(String fileName, String encoding)
  {
    this.fileName = fileName;
    this.encoding = encoding;
  }

  public Deck loadDeck()
  {
    BufferedReader inputCsv = openInputReader();

    try
    {
      return new DeckList(loadCards(inputCsv));
    }
    finally
    {
      closeQuietly(inputCsv);
    }
  }

  public void saveDeckOfTomorrow(Collection<Card> cards)
  {
    saveFor(dirNameForDayOfTodayPlus(1), cards);
  }

  public void saveDeckOfTheDayAfterTomorrow(Collection<Card> cards)
  {
    saveFor(dirNameForDayOfTodayPlus(2), cards);
  }

  private List<Card> loadCards(BufferedReader inputCsv)
  {
    List<Card> cards = new LinkedList();
    skipTheHeader(inputCsv);

    String line;
    do
    {
      line = readNextLine(inputCsv);

      if (line != null)
      {
        String[] cardValues = line.split("\\|");
        cards.add(new Card(cardValues[0], cardValues[1]));
      }
    }
    while (line != null);
    return cards;
  }

  private BufferedReader openInputReader()
  {
    BufferedReader inputCsv;
    try
    {
      inputCsv = new BufferedReader(
          new InputStreamReader(
              new FileInputStream(
                  new File(
                      dirNameForDayOfTodayPlus(0),
                      fileName)
              ),
              encoding
          )
      );
    }
    catch (Exception ex)
    {
      throw new DeckPersistenceException("Error opening deck file.", ex);
    }
    return inputCsv;
  }

  private void saveFor(String dirName, Collection<Card> cards)
  {
    BufferedWriter ous = openOutputWriter(dirName);

    try
    {
      writeCards(cards, ous);
    } finally
    {
      closeQuietly(ous);
    }
  }

  private void writeCards(Collection<Card> cards, BufferedWriter ous)
  {
    for (Card card : cards)
    {
      write(ous, card);
    }
  }

  private void write(BufferedWriter ous, Card card)
  {
    try
    {
      ous.write(card.getQuestion() + "|" + card.getAnswer());
      ous.newLine();
    }
    catch (Exception ex)
    {
      throw new DeckPersistenceException("An error occurred storing a card.", ex);
    }
  }

  private BufferedWriter openOutputWriter(String dirName)
  {
    File parent = createParentDirectory(dirName);
    File outputFile = new File(parent, this.fileName);
    boolean exists = outputFile.exists();
    BufferedWriter ous;
    try
    {
      ous = new BufferedWriter(new FileWriter(outputFile, exists));
      if(!exists)
      {
        ous.write("card question|card answer");
        ous.newLine();
      }
    }
    catch (Exception ex)
    {
      throw new DeckPersistenceException("An error occurred opening the output file ", ex);
    }
    return ous;
  }

  private File createParentDirectory(String dirName)
  {
    File parent = new File(dirName);
    if (!parent.exists())
    {
      if (!parent.mkdirs())
      {
        throw new DeckPersistenceException("Unable to create directory structure.");
      }
    }

    return parent;
  }

  private String dirNameForDayOfTodayPlus(int numOfDay)
  {
    return new SimpleDateFormat("YYYYMMdd").format(dateOfTodayPlus(numOfDay));
  }

  private Date dateOfTodayPlus(int numberOfDay)
  {
    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.DAY_OF_MONTH, numberOfDay);
    return instance.getTime();
  }

  private void skipTheHeader(BufferedReader inputCsv)
  {
    readNextLine(inputCsv);
  }

  private String readNextLine(BufferedReader inputCsv)
  {
    try
    {
      return inputCsv.readLine();
    }
    catch (Exception ex)
    {
      throw new DeckPersistenceException("Error reading deck line", ex);
    }
  }

  private void closeQuietly(Closeable closeable)
  {
    if (closeable != null)
    {
      try
      {
        closeable.close();
      }
      catch (Exception ignore)
      {

      }
    }
  }

}
