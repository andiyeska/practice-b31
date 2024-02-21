package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.UpdateBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.UpdateBookCommandResponse;

public interface UpdateBookCommand extends Command<UpdateBookCommandRequest, UpdateBookCommandResponse> {
}
