package com.practice.design.pattern.web;

import com.practice.design.pattern.command.impl.executor.CommandExecutor;
import com.practice.design.pattern.command.member.GetAllMemberCommand;
import com.practice.design.pattern.command.member.GetMemberCommand;
import com.practice.design.pattern.command.member.SaveMemberCommand;
import com.practice.design.pattern.command.member.UpdateMemberCommand;
import com.practice.design.pattern.command.model.request.member.GetAllMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.SaveMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.UpdateMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.GetAllMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.SaveMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.UpdateMemberCommandResponse;
import com.practice.design.pattern.entity.Member;
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

  private final CommandExecutor commandExecutor;

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  public Mono<Member> save(@RequestBody SaveMemberWebRequest webRequest) {
    return commandExecutor.execute(SaveMemberCommand.class, SaveMemberCommandRequest.builder()
            .name(webRequest.getName())
            .build())
        .map(SaveMemberCommandResponse::getMember);
  }

  @PutMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> update(@PathVariable(MEMBER_ID) String memberId, @RequestBody UpdateMemberWebRequest webRequest) {
    return commandExecutor.execute(UpdateMemberCommand.class, UpdateMemberCommandRequest.builder()
            .id(memberId)
            .name(webRequest.getName())
            .build())
        .map(UpdateMemberCommandResponse::getMember);
  }

  @GetMapping(MEMBER_ID_PATH)
  @ResponseStatus(HttpStatus.OK)
  public Mono<Member> get(@PathVariable(MEMBER_ID) String memberId) {
    return commandExecutor.execute(GetMemberCommand.class, GetMemberCommandRequest.builder()
            .id(memberId)
            .build())
        .map(GetMemberCommandResponse::getMember);
  }

  @GetMapping
  @ResponseStatus(HttpStatus.OK)
  public Mono<List<Member>> getAll() {
    return commandExecutor.execute(GetAllMemberCommand.class, GetAllMemberCommandRequest.builder()
            .build())
        .map(GetAllMemberCommandResponse::getMembers);
  }

}
