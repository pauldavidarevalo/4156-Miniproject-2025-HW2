package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.mockito.Mockito;


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
    testBooks.add(new Book("Pride and Prejudice", 3));
    testBooks.add(new Book("1984", 4));
    testBooks.add(new Book("To Kill a Mockingbird", 5));
    testBooks.add(new Book("The Great Gatsby", 6));
    testBooks.add(new Book("Moby Dick", 7));
    testBooks.add(new Book("Jane Eyre", 8));
    testBooks.add(new Book("The Catcher in the Rye", 9));
    testBooks.add(new Book("The Hobbit", 10));
    testBooks.add(new Book("Crime and Punishment", 11));
    testBooks.add(new Book("War and Peace", 12));
    testBooks.add(new Book("The Odyssey", 13));
    testBooks.add(new Book("The Brothers Karamazov", 14));
    testBooks.add(new Book("Brave New World", 15));

    service.getBooks().clear();
    service.getBooks().addAll(testBooks);
  }

  @Test
  void testGetBooks() {
    assertEquals(15, service.getBooks().size());
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
    assertEquals(15, service.getBooks().size());
  }

  @Test
  void testPrintBooks() {
    service.printBooks();
    assertTrue(service.getBooks().size() > 0);
  }

  @Test
  void constructorHandlesParsingException() throws Exception {

    InputStream is = new ByteArrayInputStream("malformed json".getBytes());

    ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader mockLoader = Mockito.mock(ClassLoader.class);
    Mockito.when(mockLoader.getResourceAsStream("mockdata/books.json")).thenReturn(is);

    Thread.currentThread().setContextClassLoader(mockLoader);

    MockApiService service = new MockApiService();

    assertTrue(service.getBooks() == null || service.getBooks().isEmpty());


    Thread.currentThread().setContextClassLoader(originalLoader);
  }

  @Test
  void constructorLoadsBooksSuccessfully() throws Exception {

    String json = "[{\"title\":\"Test Book\",\"id\":1}]";
    InputStream is = new ByteArrayInputStream(json.getBytes());

    ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader mockLoader = Mockito.mock(ClassLoader.class);
    Mockito.when(mockLoader.getResourceAsStream("mockdata/books.json")).thenReturn(is);

    Thread.currentThread().setContextClassLoader(mockLoader);

    MockApiService service = new MockApiService();
    assertEquals(1, service.getBooks().size());
    assertEquals("Test Book", service.getBooks().get(0).getTitle());


    Thread.currentThread().setContextClassLoader(originalLoader);
  }

  @Test
  void constructorHandlesMissingFile() {
    ClassLoader originalLoader = Thread.currentThread().getContextClassLoader();
    ClassLoader mockLoader = Mockito.mock(ClassLoader.class);
    Mockito.when(mockLoader.getResourceAsStream("mockdata/books.json")).thenReturn(null);
    Thread.currentThread().setContextClassLoader(mockLoader);

    MockApiService service = new MockApiService();
    assertTrue(service.getBooks().isEmpty());

    Thread.currentThread().setContextClassLoader(originalLoader);
  }

}
