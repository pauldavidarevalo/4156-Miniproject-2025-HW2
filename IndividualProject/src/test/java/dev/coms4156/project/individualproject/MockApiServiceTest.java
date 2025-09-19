package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


/**
 * This class contains the unit tests for the MockApiService class.
 */
public class MockApiServiceTest {
  private  MockApiService service;

  @BeforeEach
  void setUp() {
    service = new MockApiService();

    // Inject books manually to avoid dependency on JSON file
    ArrayList<Book> testBooks = new ArrayList<>();
    testBooks.add(new Book("Frankenstein", 1));
    testBooks.add(new Book("Wuthering Heights", 2));
    service.getBooks().clear();
    service.getBooks().addAll(testBooks);
  }

  @Test
  void testGetBooks() {
    assertEquals(2, service.getBooks().size());
  }

  @Test
  void testUpdateBookReplacesExisting() {
    Book updatedBook = new Book("Frankenstein", 1);
    service.updateBook(updatedBook);
    assertEquals("Frankenstein", service.getBooks().get(0).getTitle());
  }

  @Test
  void testUpdateBookNonExisting() {
    Book nonExistingBook = new Book("Jane Eyre", 99);
    service.updateBook(nonExistingBook);
    assertEquals(2, service.getBooks().size());
  }

  @Test
  void testPrintBooks() {
    service.printBooks();
    assertTrue(service.getBooks().size() > 0);
  }
}
