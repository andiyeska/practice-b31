package com.practice.design.pattern.command.impl.member;

import com.practice.design.pattern.command.member.SaveMemberCommand;
import com.practice.design.pattern.command.member.UpdateMemberCommand;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.UpdateMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.SaveMemberCommandResponse;
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
public class UpdateMemberCommandImpl implements UpdateMemberCommand {

  private static final String MEMBER_ID = "member_id";

  private final MemberRepository memberRepository;

  @Override
  public Mono<UpdateMemberCommandResponse> execute(UpdateMemberCommandRequest commandRequest) {
    return memberRepository.findById(commandRequest.getId())
        .switchIfEmpty(Mono.error(new DataNotFoundException(Member.class, Map.of(MEMBER_ID, commandRequest.getId()))))
        .map(updatedMember -> updatedMember.toBuilder()
            .name(commandRequest.getName())
            .build())
        .flatMap(memberRepository::save)
        .map(member -> UpdateMemberCommandResponse.builder()
            .member(member)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully update member with id: {}", commandResponse.getMember().getId()));
  }

}
