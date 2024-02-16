package com.practice.design.pattern.command.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBookCommandRequest {
  private String id;
  private String name;
  private int quantity;
}
