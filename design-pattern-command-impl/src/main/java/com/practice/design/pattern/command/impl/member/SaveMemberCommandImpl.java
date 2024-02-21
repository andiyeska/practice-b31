package com.practice.design.pattern.command.impl.member;

import com.practice.design.pattern.command.member.SaveMemberCommand;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.SaveMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.SaveMemberCommandResponse;
import com.practice.design.pattern.entity.Member;
import com.practice.design.pattern.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class SaveMemberCommandImpl implements SaveMemberCommand {

  private static final String MEMBER_ID = "member_id";

  private final MemberRepository memberRepository;

  @Override
  public Mono<SaveMemberCommandResponse> execute(SaveMemberCommandRequest commandRequest) {
    return memberRepository.save(Member.builder()
            .name(commandRequest.getName())
            .build())
        .map(member -> SaveMemberCommandResponse.builder()
            .member(member)
            .build())
        .doOnSuccess(commandResponse -> log.info("Successfully save member with id: {}", commandResponse.getMember().getId()));
  }

}
