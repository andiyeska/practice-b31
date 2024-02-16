package com.practice.design.pattern.command;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.SaveBookCommandRequest;
import com.practice.design.pattern.command.model.response.SaveBookCommandResponse;

public interface SaveBookCommand extends Command<SaveBookCommandRequest, SaveBookCommandResponse> {
}
