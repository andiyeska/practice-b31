package com.practice.design.pattern.command.member;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.member.GetMemberCommandRequest;
import com.practice.design.pattern.command.model.request.member.UpdateMemberCommandRequest;
import com.practice.design.pattern.command.model.response.member.GetMemberCommandResponse;
import com.practice.design.pattern.command.model.response.member.UpdateMemberCommandResponse;

public interface GetMemberCommand extends Command<GetMemberCommandRequest, GetMemberCommandResponse> {
}
