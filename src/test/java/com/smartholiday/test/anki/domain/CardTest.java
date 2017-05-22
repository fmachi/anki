package com.smartholiday.test.anki.domain;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class CardTest
{

  @Test
  public void comparable() {
    Card a = new Card("Who is the president of USA?","Trump");
    Card theSameOfA = new Card("Who is the president of USA?","Trump");

    assertThat(a.compareTo(a),is(equalTo(0)));
    assertThat(a.compareTo(theSameOfA),is(equalTo(0)));
    assertThat(theSameOfA.compareTo(a),is(equalTo(0)));

    Card b = new Card("How many states are in EU?","Twenty Five");

    assertThat(a.compareTo(b) > 0,is(true));
    assertThat(b.compareTo(a) < 0,is(true));
  }

}
