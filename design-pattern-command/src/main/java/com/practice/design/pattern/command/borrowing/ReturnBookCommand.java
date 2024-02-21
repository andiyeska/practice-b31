package com.practice.design.pattern.command.borrowing;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.borrowing.BorrowBookCommandRequest;
import com.practice.design.pattern.command.model.request.borrowing.ReturnBookCommandRequest;
import com.practice.design.pattern.command.model.response.borrowing.BorrowBookCommandResponse;
import com.practice.design.pattern.command.model.response.borrowing.ReturnBookCommandResponse;

public interface ReturnBookCommand extends Command<ReturnBookCommandRequest, ReturnBookCommandResponse> {
}
