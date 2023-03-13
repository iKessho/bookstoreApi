package com.example.bookstoreApi.controller;

import com.example.bookstoreApi.domain.Author;
import com.example.bookstoreApi.service.AuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/author")
public class AuthorController {

    @Autowired
    private AuthorService authorService;

    @GetMapping
    public ResponseEntity<?> searchAuthors() {
        List<Author> result = new ArrayList<>();
        authorService.getAuthors().forEach(result::add);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping
    public ResponseEntity<?> createAuthor(@RequestBody Author author) {
        Author createdAuthor = authorService.createAuthor(author);
        return ResponseEntity.created(URI.create("/author/" + createdAuthor.getId())).build();
    }
}
