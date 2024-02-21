package com.practice.design.pattern.command.member;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.SaveMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.SaveMemberCommandResponse;

public interface SaveMemberCommand extends Command<SaveMemberCommandRequest, SaveMemberCommandResponse> {
}
