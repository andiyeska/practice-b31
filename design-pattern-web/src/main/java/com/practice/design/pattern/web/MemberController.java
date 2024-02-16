package com.practice.design.pattern.web;

import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.MemberRepository;
import com.practice.design.pattern.web.model.member.SaveMemberWebRequest;
import com.practice.design.pattern.web.model.member.UpdateMemberWebRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(MemberController.BASE_PATH)
public class MemberController {

  public static final String BASE_PATH = "/members";
  public static final String MEMBER_ID = "member_id";
  public static final String MEMBER_ID_PATH = "/{" + MEMBER_ID + "}";

  private final MemberRepository memberRepository;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Member> save(@RequestBody SaveMemberWebRequest webRequest) {
    return memberRepository.save(Member.builder()
            .name(webRequest.getName())
            .build())
        .doOnSuccess(member -> log.info("Successfully save member with id: {}", member.getId()));
  }

  @PutMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> update(@PathVariable(MEMBER_ID) String memberId, @RequestBody UpdateMemberWebRequest webRequest) {
    return memberRepository.findById(memberId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, memberId))))
        .map(member -> member.toBuilder()
            .name(webRequest.getName())
            .build())
        .flatMap(memberRepository::save)
        .doOnSuccess(member -> log.info("Successfully update member with id: {}", member.getId()));
  }

  @GetMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> get(@PathVariable(MEMBER_ID) String memberId) {
    return memberRepository.findById(memberId)
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, memberId))))
        .doOnSuccess(member -> log.info("Successfully update member with id: {}", member.getId()));
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Flux<Member> getAll() {
    return memberRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, new HashMap<>())))
        .doOnNext(member -> log.info("Successfully get all members"));
  }

}
