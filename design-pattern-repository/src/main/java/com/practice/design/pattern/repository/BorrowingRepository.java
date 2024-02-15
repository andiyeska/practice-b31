package com.practice.design.pattern.repository;

import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Member;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BorrowingRepository extends ReactiveMongoRepository<Borrowing, String> {
}
