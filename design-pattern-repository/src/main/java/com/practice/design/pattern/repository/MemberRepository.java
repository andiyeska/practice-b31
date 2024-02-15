package com.practice.design.pattern.repository;

import com.practice.design.pattern.entity.Book;
import com.practice.design.pattern.entity.Member;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface MemberRepository extends ReactiveMongoRepository<Member, String> {
}
