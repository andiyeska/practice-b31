package com.practice.design.pattern.web;

import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.service.MemberService;
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
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(MemberController.BASE_PATH)
public class MemberController {

  public static final String BASE_PATH = "/members";
  public static final String MEMBER_ID = "member_id";
  public static final String MEMBER_ID_PATH = "/{" + MEMBER_ID + "}";

  private final MemberService memberService;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Member> save(@RequestBody SaveMemberWebRequest webRequest) {
    return memberService.save(
        Member.builder()
            .name(webRequest.getName())
            .build());
  }

  @PutMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> update(@PathVariable(MEMBER_ID) String memberId, @RequestBody UpdateMemberWebRequest webRequest) {
    return memberService.update(
        Member.builder()
            .id(memberId)
            .name(webRequest.getName())
            .build());
  }

  @GetMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> get(@PathVariable(MEMBER_ID) String memberId) {
    return memberService.get(memberId);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<List<Member>> getAll() {
    return memberService.getAll();
  }

}
