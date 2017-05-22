package com.smartholiday.test.anki.adapters;

import com.smartholiday.test.anki.domain.Card;
import com.smartholiday.test.anki.domain.Deck;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FileDeckRepositoryTest
{

  CSVFileDeckRepository repository = new CSVFileDeckRepository("deck.csv", "UTF-8");

  @Before
  public void setup() throws IOException
  {
    FileUtils.deleteDirectory(new File(createDirNameFor(today())));
    FileUtils.deleteDirectory(new File(createDirNameFor(tomorrow())));
    FileUtils.deleteDirectory(new File(createDirNameFor(dayAfterTomorrow())));
  }

  @After
  public void after() throws IOException
  {
    FileUtils.deleteDirectory(new File(createDirNameFor(today())));
    FileUtils.deleteDirectory(new File(createDirNameFor(tomorrow())));
    FileUtils.deleteDirectory(new File(createDirNameFor(dayAfterTomorrow())));
  }

  @Test
  public void loadCards() throws IOException
  {
    String dirNameForToday = createDirNameFor(today());
    createDirectory(dirNameForToday);

    copyFile("src/test/resources/deck.csv", dirNameForToday, "deck.csv");

    Deck deck = repository.loadDeck();
    assertThat(deck.hasNext(), is(true));
    assertCardIs(deck.next(), "How is dietary cholesterol transported to target tissues?", "In chylomicrons");
    assertThat(deck.hasNext(), is(true));
    assertCardIs(deck.next(), "What enzyme breaks down sugars mouth and digestive tract?", "Amylase");
    assertThat(deck.hasNext(), is(true));
    assertCardIs(deck.next(),
                 "What is the glucose transporter in the brain and what are its properties?",
                 "GLUT-1 transports glucose across blood-brain barrier, GLUT-3 transports glucose into neurons.  Both are high-affinity.");
    assertThat(deck.hasNext(), is(false));
  }

  @Test
  public void saveCardsTomorrow() throws IOException
  {
    String dirNameForTomorrow = createDirNameFor(tomorrow());
    createDirectory(dirNameForTomorrow);

    repository.saveDeckOfTomorrow(
        asList(
            new Card("What enzyme breaks down sugars mouth and digestive tract?", "Amylase"),
            new Card("How is dietary cholesterol transported to target tissues?", "In chylomicrons"),
            new Card("What is the glucose transporter in the brain and what are its properties?", "GLUT-1 transports glucose across blood-brain barrier, GLUT-3 transports glucose into neurons.  Both are high-affinity.")
        ));

    assertThat(FileUtils.contentEquals(new File("src/test/resources/deck.csv"),new File(dirNameForTomorrow,"deck.csv")),is(true));

  }

  @Test
  public void saveCardsDayAfterTomorrow() throws IOException
  {
    String dirNameForDayAfterTomorrow = createDirNameFor(dayAfterTomorrow());
    createDirectory(dirNameForDayAfterTomorrow);

    repository.saveDeckOfTheDayAfterTomorrow(
        asList(
            new Card("What enzyme breaks down sugars mouth and digestive tract?", "Amylase"),
            new Card("How is dietary cholesterol transported to target tissues?", "In chylomicrons"),
            new Card("What is the glucose transporter in the brain and what are its properties?", "GLUT-1 transports glucose across blood-brain barrier, GLUT-3 transports glucose into neurons.  Both are high-affinity.")
        ));

    assertThat(FileUtils.contentEquals(new File("src/test/resources/deck.csv"),new File(dirNameForDayAfterTomorrow,"deck.csv")),is(true));

  }

  private void assertCardIs(Card card, String question, String answer)
  {
    assertThat(card.getQuestion(), is(equalTo(question)));
    assertThat(card.getAnswer(), is(equalTo(answer)));
  }

  private void copyFile(String origin, String directory, String fileName) throws IOException
  {
    IOUtils.copy(new FileInputStream(origin), new FileOutputStream(new File(directory, fileName)));
  }

  private void createDirectory(String dirName)
  {
    File dir = new File(dirName);
    assertThat(dir.exists() || dir.mkdir(), is(true));
  }

  private String createDirNameFor(Date today)
  {
    return new SimpleDateFormat("YYYYMMdd").format(today);
  }

  private Date today()
  {
    return new Date();
  }

  private Date tomorrow()
  {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DAY_OF_MONTH, 1);
    return date.getTime();
  }

  private Date dayAfterTomorrow()
  {
    Calendar date = Calendar.getInstance();
    date.add(Calendar.DAY_OF_MONTH, 2);
    return date.getTime();
  }

}
