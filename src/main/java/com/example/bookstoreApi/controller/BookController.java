package com.example.bookstoreApi.controller;

import com.example.bookstoreApi.domain.Book;
import com.example.bookstoreApi.service.BookService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/book")
public class BookController {

    @Autowired
    private BookService bookService;

    @GetMapping
    public ResponseEntity<?> searchBooks(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false, defaultValue = "and") String operator) {
        List<Book> result;

        if (title != null && authorName != null) {
            result = bookService.findByTitleAndAuthors(title, authorName, operator);
        } else if (title != null) {
            result = bookService.findByTitle(title);
        } else if (authorName != null) {
            result = bookService.findByAuthorName(authorName);
        } else {
            result = bookService.findAllBooks();
        }

        if (result.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> searchBookById(
            @PathVariable("id") String id) {
        Book result = bookService.findBookById(id);

        if (result == null) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);
        }

        return ResponseEntity.ok(result);
    }

    @PostMapping
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        Book createdBook = bookService.createBook(book);
        return ResponseEntity.created(URI.create("/book/" + createdBook.getIsbn())).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable("id") String isbn,
            @RequestBody Book book) {
        Book updatedBook = bookService.updateBook(isbn, book);

        return ResponseEntity.ok(updatedBook);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable("id") String isbn){
        bookService.deleteBook(isbn);
        return ResponseEntity.ok().build();
    }
}
