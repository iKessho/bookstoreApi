package com.example.bookstoreApi;

import com.example.bookstoreApi.domain.Author;
import com.example.bookstoreApi.domain.Book;
import com.example.bookstoreApi.repository.AuthorRepository;
import com.example.bookstoreApi.repository.BookRepository;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class DataLoader implements ApplicationRunner {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private Faker faker;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        int noAuthor = 2;
        int noBook = 5;
        List<Author> authorList = new ArrayList<>();
        for (int i = 0; i < noAuthor; i++) {
            Author author = new Author();
            author.setBirthday(faker.date().birthday());
            author.setName(faker.name().name().toString());
            Author savedAuthor = authorRepository.save(author);
            authorList.add(savedAuthor);
        }

        for (int i = 0; i < noBook; i++) {
            Book book = new Book();
            com.github.javafaker.Book fakeBook = faker.book();
            book.setIsbn(faker.number().digits(10));
            book.setTitle(fakeBook.title());
            book.setGenre(fakeBook.genre());
            book.setPrice(1.0);
            book.setYear(faker.date().birthday().getYear());
            for (int j = 0;  j < faker.number().numberBetween(1,noAuthor); j++){
                book.getAuthors().add(authorList.get(j));
                authorList.get(j).getBooks().add(book);
            }
            bookRepository.save(book);

        }

    }
}
