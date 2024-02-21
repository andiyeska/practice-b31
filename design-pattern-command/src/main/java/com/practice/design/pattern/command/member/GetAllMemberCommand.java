package com.practice.design.pattern.command.member;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.member.GetAllMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.GetAllMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;

public interface GetAllMemberCommand extends Command<GetAllMemberCommandRequest, GetAllMemberCommandResponse> {
}
