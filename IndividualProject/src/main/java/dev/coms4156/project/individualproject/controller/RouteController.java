package dev.coms4156.project.individualproject.controller;

import dev.coms4156.project.individualproject.model.Book;
import dev.coms4156.project.individualproject.service.MockApiService;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * This class contains all the API routes for the application.
 */
@RestController
public class RouteController {

  private final MockApiService mockApiService;

  public RouteController(MockApiService mockApiService) {
    this.mockApiService = mockApiService;
  }

  @GetMapping({"/", "/index"})
  public String index() {
    return "Welcome to the home page! In order to make an API call direct your browser"
        + "or Postman to an endpoint.";
  }

  /**
   * Returns the details of the specified book.
   *
   * @param id An {@code int} representing the unique identifier of the book to retrieve.
   *
   * @return A {@code ResponseEntity} containing either the matching {@code Book} object with an
   *         HTTP 200 response, or a message indicating that the book was not
   *         found with an HTTP 404 response.
   */
  @GetMapping({"/book/{id}"})
  public ResponseEntity<?> getBook(@PathVariable int id) {
    for (Book book : mockApiService.getBooks()) {
      if (book.getId() == id) {
        return new ResponseEntity<>(book, HttpStatus.OK);
      }
    }

    return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
  }

  /**
   * Returns a list of all the books with available copies.
   *
   * @return A {@code ResponseEntity} containing a list of available {@code Book} objects with an
   *         HTTP 200 response if sucessful, or a message indicating an error occurred with an
   *         HTTP 500 response.
   */
  @GetMapping({"/books/available"})
  public ResponseEntity<?> getAvailableBooks() {
    try {
      ArrayList<Book> availableBooks = new ArrayList<>();

      for (Book book : mockApiService.getBooks()) {
        if (book.hasCopies()) {
          availableBooks.add(book);
        }
      }

      return new ResponseEntity<>(availableBooks, HttpStatus.OK);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>("Error occurred when getting all available books",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Returns a list of recommended books.
   *
   * @return A {@code ResponseEntity} containing a list of recommended {@code Book} objects with an
   *         HTTP 200 response if sucessful, or a message indicating an error occurred with an
   *         HTTP 500 response.
   */
  @GetMapping({"/books/recommendation"})
  public ResponseEntity<?> getRecommendedBooks() {
    try {
      ArrayList<Book> books = new ArrayList<>(mockApiService.getBooks());
      ArrayList<Book> recommendedBooks = new ArrayList<>();
      books.sort(
              Comparator.comparingInt(Book::getAmountOfTimesCheckedOut).reversed()
      );
      int limit = Math.min(5, books.size());
      for (int i = 0; i < limit; i++) {
        recommendedBooks.add(books.get(i));
      }

      Set<Integer> usedIndices = new HashSet<>();
      while (usedIndices.size() < 5 && recommendedBooks.size() < books.size()) {
        int i = (int)(Math.random() * books.size());
        Book candidate = books.get(i);

        if (!recommendedBooks.contains(candidate)) {
          recommendedBooks.add(candidate);
          usedIndices.add(i);
        }
      }

      return new ResponseEntity<>(recommendedBooks, HttpStatus.OK);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>("Error occurred when getting recommended books",
              HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Updates the information of a target book with checkoutCopy() then updates the books
   * array list in MockApiService.java using updateBook()
   *
   * @param id of a book to update its check out information.
   *
   */
  @PostMapping("/checkout")
  public ResponseEntity<?> checkoutBook(@RequestParam("id") int id) {
    try {
      ArrayList<Book> books = new ArrayList<>(mockApiService.getBooks());
      Book target = null;
      for (Book b : books) {
        if (b.getId() == id) {
          target = b;
          break;
        }
      }

      if (target == null) {
        return new ResponseEntity<>("Book with ID " + id + " not found.", HttpStatus.NOT_FOUND);
      }

      String dueDate = target.checkoutCopy();
      if (dueDate == null) {
        return new ResponseEntity<>("No available copies of Book with ID " + id + ".", HttpStatus.CONFLICT);
      }
      mockApiService.updateBook(target);
      return new ResponseEntity<>(target, HttpStatus.OK);

    } catch (Exception e) {
      return new ResponseEntity<>("An error occurred while checking out the book.", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Adds a copy to the {@code} Book object if it exists.
   *
   * @param bookId An {@code Integer} representing the unique id of the book.
   * @return A {@code ResponseEntity} containing the updated {@code Book} object with an
   *         HTTP 200 response if successful or HTTP 404 if the book is not found,
   *         or a message indicating an error occurred with an HTTP 500 code.
   */
  @PatchMapping({"/book/{bookId}/add"})
  public ResponseEntity<?> addCopy(@PathVariable Integer bookId) {
    try {
      for (Book book : mockApiService.getBooks()) {
        if (bookId.equals(book.getId())) {
          book.addCopy();
          return new ResponseEntity<>(book, HttpStatus.OK);
        }
      }

      return new ResponseEntity<>("Book not found.", HttpStatus.NOT_FOUND);
    } catch (Exception e) {
      System.err.println(e);
      return new ResponseEntity<>("Error occurred when adding a copy.",
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
