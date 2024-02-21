package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.GetBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.GetBookCommandResponse;

public interface GetBookCommand extends Command<GetBookCommandRequest, GetBookCommandResponse> {
}
