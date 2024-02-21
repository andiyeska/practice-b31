package com.practice.design.pattern.command.impl.member;

import com.practice.design.pattern.command.member.GetAllMemberCommand;
import com.practice.design.pattern.command.member.GetMemberCommand;
import com.practice.design.pattern.command.model.request.member.GetAllMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.GetAllMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetAllMemberCommandImpl implements GetAllMemberCommand {

  private static final String MEMBER_ID = "member_id";

  private final MemberRepository memberRepository;

  @Override
  public Mono<GetAllMemberCommandResponse> execute(GetAllMemberCommandRequest commandRequest) {
    return memberRepository.findAll()
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, new HashMap<>())))
        .collectList()
        .map(members -> GetAllMemberCommandResponse.builder()
            .members(members)
            .build())
        .doOnNext(member -> log.info("Successfully get all members"));
  }

}
