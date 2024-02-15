package com.practice.design.pattern.repository;

import com.practice.design.pattern.entity.Book;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BookRepository extends ReactiveMongoRepository<Book, String> {
}
