package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.SaveBookCommandResponse;

public interface SaveBookCommand extends Command<SaveBookCommandRequest, SaveBookCommandResponse> {
}
