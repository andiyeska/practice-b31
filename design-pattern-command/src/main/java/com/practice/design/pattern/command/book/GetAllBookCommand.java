package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.GetAllBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.GetAllBookCommandResponse;

public interface GetAllBookCommand extends Command<GetAllBookCommandRequest, GetAllBookCommandResponse> {
}
