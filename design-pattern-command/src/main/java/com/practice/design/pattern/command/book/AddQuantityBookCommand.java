package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.AddQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.AddQuantityBookCommandResponse;

public interface AddQuantityBookCommand extends Command<AddQuantityBookCommandRequest, AddQuantityBookCommandResponse> {
}
