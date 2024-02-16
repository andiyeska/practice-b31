package com.practice.design.pattern.service;

import com.practice.design.pattern.entity.Book;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface BookService {
  Mono<Book> save(Book book);
  Mono<Book> update(Book book);
  Mono<Book> get(String id);
  Mono<List<Book>> getAll();
  Mono<Book> addQuantity(String id, int addedQuantity);
  Mono<Book> reduceQuantity(String id, int reducedQuantity);
}
