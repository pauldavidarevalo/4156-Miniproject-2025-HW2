package dev.coms4156.project.individualproject;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

  @BeforeEach
  void setUp() {
    mockApiService = mock(MockApiService.class);
    routeController = new RouteController(mockApiService);
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
}
