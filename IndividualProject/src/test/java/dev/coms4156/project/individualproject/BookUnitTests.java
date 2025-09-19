package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.model.Book;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * This class contains the unit tests for the Book class.
 */
@SpringBootTest
public class BookUnitTests {

  public static Book book;

  @BeforeAll
  public static void setUpBookForTesting() {
    book = new Book("When Breath Becomes Air", 0);
  }

  @Test
  public void equalsBothAreTheSameTest() {
    Book cmpBook = book;
    assertEquals(cmpBook, book);
    Book sameIdBook = new Book("Little Women", 0);
    assertEquals(sameIdBook, book);

    Book diffBook = new Book("Moby Dick", 2);
    assertNotEquals(diffBook, book);

    assertNotEquals(book, null);
    assertNotEquals(book, "NotABook");
  }

  @Test
  public void hasMultipleAuthorsTest() {
    assertFalse(book.hasMultipleAuthors());

    ArrayList<String> authors = new ArrayList<>();
    authors.add("Emily Bronte");
    book.setAuthors(authors);
    assertFalse(book.hasMultipleAuthors());
    authors.add("Mary Shelley");
    book.setAuthors(authors);
    assertTrue(book.hasMultipleAuthors());
  }

  @Test
  public void deleteCopyTest() {
    book.setTotalCopies(1);
    assertFalse(book.deleteCopy());
    assertTrue(book.deleteCopy());
    book.setTotalCopies(0);
    assertTrue(book.deleteCopy());
  }

  @Test
  public void checkoutCopyTest() {
    book.setTotalCopies(1);
    String dueDate = book.checkoutCopy();
    assertNotNull(dueDate);
    assertEquals(0, book.getCopiesAvailable());
    book.setTotalCopies(0);
    assertNull(book.checkoutCopy());
  }

  @Test
  public void returnCopyTest() {
    book.setReturnDates(new ArrayList<>());
    assertFalse(book.returnCopy("2025-12-31"));
    ArrayList<String> dates = new ArrayList<>();
    dates.add("2025-12-31");
    book.setReturnDates(dates);
    assertFalse(book.returnCopy("2025-01-01"));
    assertTrue(book.returnCopy("2025-12-31"));
    assertEquals(1, book.getCopiesAvailable());
  }

  @Test
  public void compareToTest() {
    Book book2 = new Book("Call of the Wild", 2);
    assertTrue(book.compareTo(book2) < 0);
    assertTrue(book2.compareTo(book) > 0);
    Book book3 = new Book("Fahrenheit 451", 0);
    assertEquals(0, book.compareTo(book3));
  }

  @Test
  public void toStringTest() {
    String str = book.toString();
    assertTrue(str.contains("When Breath Becomes Air"));
  }

  @Test
  public void gettersSettersTest() {
    book.setTitle("The Great Gatsby");
    assertEquals("The Great Gatsby", book.getTitle());

    ArrayList<String> authors = new ArrayList<>();
    authors.add("JRR Tolkien");
    book.setAuthors(authors);
    assertEquals(authors, book.getAuthors());

    book.setLanguage("English");
    assertEquals("English", book.getLanguage());

    book.setShelvingLocation("Shelf A");
    assertEquals("Shelf A", book.getShelvingLocation());

    book.setPublicationDate("2025-09-12");
    assertEquals("2025-09-12", book.getPublicationDate());

    book.setPublisher("Penguin");
    assertEquals("Penguin", book.getPublisher());

    ArrayList<String> subjects = new ArrayList<>();
    subjects.add("Underwater Basket Weaving");
    book.setSubjects(subjects);
    assertEquals(subjects, book.getSubjects());

    book.setId(42);
    assertEquals(42, book.getId());

    book.setReturnDates(null);
    assertNotNull(book.getReturnDates());
  }

}
