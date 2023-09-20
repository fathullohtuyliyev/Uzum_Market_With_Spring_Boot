package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.ToString;

import java.util.Date;

@AllArgsConstructor
@ToString
public class ErrorDto {
    public String path;
    public Integer code;
    public Date date;
    public String reason;
}
