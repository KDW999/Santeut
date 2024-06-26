package com.santeut.party.feign.dto.response;

import lombok.Data;

@Data
public class FeignResponseDto<T> {
  public int status;
  public T data;
}
