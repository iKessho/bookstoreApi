package com.example.bookstoreApi.service;

import com.example.bookstoreApi.domain.Author;

import java.util.List;

public interface AuthorService {
    List<Author> getAuthors();

    Author createAuthor(Author author);
}
