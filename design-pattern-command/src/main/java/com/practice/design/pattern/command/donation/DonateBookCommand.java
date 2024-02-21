package com.practice.design.pattern.command.donation;

import com.practice.design.pattern.command.base.Command;
import com.practice.design.pattern.command.model.request.donation.DonateBookCommandRequest;
import com.practice.design.pattern.command.model.response.donation.DonateBookCommandResponse;

public interface DonateBookCommand extends Command<DonateBookCommandRequest, DonateBookCommandResponse> {
}
