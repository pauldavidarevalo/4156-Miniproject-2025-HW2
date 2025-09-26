package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import dev.coms4156.project.individualproject.controller.RouteController;
import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * This class contains the unit tests for the RouteController class.
 */
class RouteControllerTest {

  private MockApiService mockApiService;
  private RouteController routeController;
  private ArrayList<Book> testBooks;

  @BeforeEach
  void setUp() {
    mockApiService = mock(MockApiService.class);
    routeController = new RouteController(mockApiService);
    testBooks = new ArrayList<>();
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

    for (Book b : testBooks) {
      //b.setTotalCopies(10); // set total copies to 10

      // Simulate amount of times checked out (for testing popularity)
      int checkouts = b.getId();
      for (int i = 0; i < checkouts; i++) {
        b.addCopy();
        b.checkoutCopy();
      }
    }

  }

  @Test
  void indexReturnsWelcomeMessage() {
    String result = routeController.index();
    assertTrue(result.contains("Welcome to the home page"));
  }

  @Test
  void getBookReturnsBookWhenFound() {
    Book book = new Book();
    book.setId(1);
    when(mockApiService.getBooks()).thenReturn(new ArrayList<>(List.of(book)));


    ResponseEntity<?> response = routeController.getBook(1);

    assertEquals(200, response.getStatusCodeValue());
    assertEquals(book, response.getBody());
  }

  @Test
  void getBookReturnsNotFoundWhenMissing() {
    when(mockApiService.getBooks()).thenReturn(new ArrayList<>());

    ResponseEntity<?> response = routeController.getBook(999);

    assertEquals(404, response.getStatusCodeValue());
    assertEquals("Book not found.", response.getBody());
  }

  @Test
  void addCopyBookFound() {
    Book book = new Book();
    book.setId(2);
    book.setTotalCopies(0);
    when(mockApiService.getBooks()).thenReturn(new ArrayList<>(List.of(book)));


    ResponseEntity<?> response = routeController.addCopy(2);

    assertEquals(200, response.getStatusCodeValue());
    assertTrue(((Book) response.getBody()).getTotalCopies() > 0);
  }

  @Test
  void addCopyBookNotFound() {
    when(mockApiService.getBooks()).thenReturn(new ArrayList<>());

    ResponseEntity<?> response = routeController.addCopy(123);

    assertEquals(404, response.getStatusCodeValue());
  }

  @Test
  void getRecommendedBooksReturnsBooks() {
    when(mockApiService.getBooks()).thenReturn(testBooks);
    ResponseEntity<?> response = routeController.getRecommendedBooks();

    @SuppressWarnings("unchecked")
    ArrayList<Book> recommended = (ArrayList<Book>) response.getBody();

    System.out.println("Recommended books:");
    for (Book b : recommended) {
      System.out.println(b.getTitle() + " - Checked out: " + b.getAmountOfTimesCheckedOut());
    }
    assertEquals(200, response.getStatusCodeValue());
    assertTrue(recommended.size() <= 10);
    for (int i = 0; i < 5; i++) {
      assertEquals(15 - i, recommended.get(i).getAmountOfTimesCheckedOut());
    }
  }
  @Test
  void checkoutBookSystem(){
    when(mockApiService.getBooks()).thenReturn(testBooks);
    int targetId = 25;
    ResponseEntity<?> response = routeController.checkoutBook(targetId);
    assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatusCodeValue());
    targetId = 1;
    response = routeController.checkoutBook(targetId);
    assertEquals(200, response.getStatusCodeValue());
    response = routeController.checkoutBook(targetId);
    assertEquals(409, response.getStatusCodeValue());

  }
  @Test
  void getAvailableBooksHandlesException() {
    MockApiService mockService = mock(MockApiService.class);
    doThrow(new RuntimeException("fail")).when(mockService).getBooks();

    RouteController controller = new RouteController(mockService);
    ResponseEntity<?> response = controller.getAvailableBooks();

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
  }
  @Test
  void checkoutBookHandlesException() {
    MockApiService mockService = mock(MockApiService.class);
    doThrow(new RuntimeException("fail")).when(mockService).getBooks();

    RouteController controller = new RouteController(mockService);
    ResponseEntity<?> response = controller.checkoutBook(1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
  }

  @Test
  void addCopyHandlesException() {
    MockApiService mockService = mock(MockApiService.class);
    doThrow(new RuntimeException("fail")).when(mockService).getBooks();

    RouteController controller = new RouteController(mockService);
    ResponseEntity<?> response = controller.addCopy(1);

    assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getStatusCodeValue());
  }
  @Test
  void getAvailableBooksReturnsOnlyBooksWithCopies() {
    Book book1 = new Book(); book1.setTotalCopies(0); // no copies
    Book book2 = new Book(); book2.setTotalCopies(1); // has copies
    ArrayList<Book> books = new ArrayList<>();
    books.add(book1);
    books.add(book2);

    MockApiService mockService = mock(MockApiService.class);
    when(mockService.getBooks()).thenReturn(books);

    RouteController controller = new RouteController(mockService);
    ResponseEntity<?> response = controller.getAvailableBooks();

    assertEquals(HttpStatus.OK.value(), response.getStatusCodeValue());
    ArrayList<Book> available = (ArrayList<Book>) response.getBody();
    assertEquals(2, available.size());
    assertEquals(book2, available.get(0));
  }
}
