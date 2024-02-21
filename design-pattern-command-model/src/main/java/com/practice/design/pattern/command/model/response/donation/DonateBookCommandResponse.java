package com.practice.design.pattern.command.model.response.donation;

import com.practice.design.pattern.entity.Donation;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DonateBookCommandResponse {
  private Donation donation;
}
