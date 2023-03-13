package com.example.bookstoreApi.service;

import com.example.bookstoreApi.domain.Book;

import java.util.List;

public interface BookService {
    List<Book> findByTitleAndAuthors(String title, String authorName, String operator);

    List<Book> findByTitle(String title);

    List<Book> findByAuthorName(String authorName);

    List<Book> findAllBooks();

    Book findBookById(String id);

    Book createBook(Book book);

    Book updateBook(String isbn, Book book);

    void deleteBook(String isbn);
}
