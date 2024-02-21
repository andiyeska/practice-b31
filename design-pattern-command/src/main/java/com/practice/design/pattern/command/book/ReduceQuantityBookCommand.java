package com.practice.design.pattern.command.book;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.book.ReduceQuantityBookCommandRequest;
import com.practice.design.pattern.command.model.response.book.ReduceQuantityBookCommandResponse;

public interface ReduceQuantityBookCommand extends Command<ReduceQuantityBookCommandRequest, ReduceQuantityBookCommandResponse> {
}
