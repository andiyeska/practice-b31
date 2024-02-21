package com.practice.design.pattern.command.model.response.member;

import com.practice.design.pattern.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetAllMemberCommandResponse {
  private List<Member> members;
}
