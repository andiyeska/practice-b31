package com.practice.design.pattern.service.impl;

import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.MemberRepository;
import com.practice.design.pattern.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

  private static final String MEMBER_ID = "member_id";

  private final MemberRepository memberRepository;

  @Override
  public Mono<Member> save(Member member) {
    return memberRepository.save(Member.builder()
            .name(member.getName())
            .build())
        .doOnSuccess(newMember -> log.info("Successfully save member with id: {}", newMember.getId()));
  }

  @Override
  public Mono<Member> update(Member member) {
    return get(member.getId())
        .map(updatedMember -> updatedMember.toBuilder()
            .name(member.getName())
            .build())
        .flatMap(memberRepository::save)
        .doOnSuccess(updatedMember -> log.info("Successfully update member with id: {}", updatedMember.getId()));
  }

  @Override
  public Mono<Member> get(String id) {
    return memberRepository.findById(id)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, id))))
        .doOnSuccess(member -> log.info("Successfully update member with id: {}", member.getId()));
  }

  @Override
  public Mono<List<Member>> getAll() {
    return memberRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, new HashMap<>())))
        .collectList()
        .doOnNext(member -> log.info("Successfully get all members"));
  }
}
