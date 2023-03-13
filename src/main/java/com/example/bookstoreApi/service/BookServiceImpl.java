package com.example.bookstoreApi.service;

import com.example.bookstoreApi.domain.Author;
import com.example.bookstoreApi.domain.Book;
import com.example.bookstoreApi.repository.AuthorRepository;
import com.example.bookstoreApi.repository.BookRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BookServiceImpl implements BookService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    private static final Logger logger = LogManager.getLogger(BookServiceImpl.class);

    @Override
    public List<Book> findByTitleAndAuthors(String title, String authorName, String operator) {
        if (operator.equalsIgnoreCase("or")) {
            return bookRepository.findDistinctByTitleContainingIgnoreCaseOrAuthorsNameContainingIgnoreCase(title, authorName);
        } else {
            return bookRepository.findDistinctByTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(title, authorName);
        }
    }

    @Override
    public List<Book> findByTitle(String title) {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> findByAuthorName(String authorName) {
        return bookRepository.findDistinctByAuthorsNameContainingIgnoreCase(authorName);
    }

    @Override
    public List<Book> findAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public Book findBookById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    @Transactional
    public Book createBook(Book book) {
        Optional<Book> existingBook = bookRepository.findById(book.getIsbn());
        if (existingBook.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Book exists with ISBN: " + book.getIsbn());
        }

        Set<Author> authors = book.getAuthors();
        Set<Author> updatedAuthors = new HashSet<>();

        for (Author author : authors) {
            Optional<Author> existingAuthor = authorRepository.findById(author.getId());
            if (existingAuthor.isPresent()) {
                existingAuthor.get().addBook(book);
                updatedAuthors.add(existingAuthor.get());
            } else {
                existingAuthor = authorRepository.findByName(author.getName());
                if (existingAuthor.isPresent()) {
                    existingAuthor.get().addBook(book);
                    updatedAuthors.add(existingAuthor.get());
                } else {
                    Author newAuthor = authorRepository.save(author);
                    newAuthor.addBook(book);
                    updatedAuthors.add(newAuthor);
                }
            }
        }
        book.setAuthors(updatedAuthors);
        return bookRepository.save(book);
    }

    @Override
    public Book updateBook(String isbn, Book book) {
        Optional<Book> existingBook = bookRepository.findById(isbn);
        if (existingBook.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("User {} updated book with id {}", authentication.getName(), isbn);
            return bookRepository.save(book);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book with ISBN: " +  isbn);
        }
    }

    @Override
    public void deleteBook(String isbn) {
        Optional<Book> existingBook = bookRepository.findById(isbn);
        if (existingBook.isPresent()) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            logger.info("User {} deleted book with id {}", authentication.getName(), isbn);
            bookRepository.deleteById(isbn);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Unable to find book with ISBN: " +  isbn);
        }
    }

}
