package com.example.bookstoreApi.repository;

import com.example.bookstoreApi.domain.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {

    List<Book> findDistinctByTitleContainingIgnoreCaseOrAuthorsNameContainingIgnoreCase(String title, String authorName);

    List<Book> findDistinctByTitleContainingIgnoreCaseAndAuthorsNameContainingIgnoreCase(String title, String authorName);

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findDistinctByAuthorsNameContainingIgnoreCase(String authorName);
}
