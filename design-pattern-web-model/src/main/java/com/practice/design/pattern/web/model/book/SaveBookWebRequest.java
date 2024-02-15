package com.practice.design.pattern.service.model.book;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SaveBookWebRequest {
  private String id;
  private String name;
  private int quantity;
}
