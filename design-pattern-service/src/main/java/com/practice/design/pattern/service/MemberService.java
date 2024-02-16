package com.practice.design.pattern.service;

import com.practice.design.pattern.entity.Borrowing;
import com.practice.design.pattern.entity.Member;
import reactor.core.publisher.Mono;

import java.util.List;

public interface MemberService {
  Mono<Member> save(Member member);
  Mono<Member> update(Member member);
  Mono<Member> get(String id);
  Mono<List<Member>> getAll();
}
