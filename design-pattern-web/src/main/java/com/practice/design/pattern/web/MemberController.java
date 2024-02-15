package com.practice.design.pattern.web;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController(MemberController.BASE_PATH)
public class MemberController {

  public static final String BASE_PATH = "/members";
  public static final String MEMBER_ID = "/{member_id}";

}
