package com.example.bookstoreApi.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Getter @Setter @NoArgsConstructor
public class Book {

    @Id
    private String isbn;

    private String title;

    @ManyToMany
    private Set<Author> authors = new HashSet<>();

    @Column(name = "`YEAR`")
    private Integer year;

    private Double price;

    private String genre;
}
