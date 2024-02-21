package com.practice.design.pattern.command.impl.member;

import com.practice.design.pattern.command.member.GetMemberCommand;
import com.practice.design.pattern.command.member.UpdateMemberCommand;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.UpdateMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.UpdateMemberCommandResponse;
import com.practice.design.pattern.common.exception.DataNotFoundException;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class GetMemberCommandImpl implements GetMemberCommand {

  private static final String MEMBER_ID = "member_id";

  private final MemberRepository memberRepository;

  @Override
  public Mono<GetMemberCommandResponse> execute(GetMemberCommandRequest commandRequest) {
    return memberRepository.findById(commandRequest.getId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, commandRequest.getId()))))
        .map(member -> GetMemberCommandResponse.builder()
            .member(member)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully update member with id: {}", commandResponse.getMember().getId()));
  }

}
