package com.practice.design.pattern.command.member;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.request.member.UpdateMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.SaveMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.UpdateMemberCommandResponse;

public interface UpdateMemberCommand extends Command<UpdateMemberCommandRequest, UpdateMemberCommandResponse> {
}
