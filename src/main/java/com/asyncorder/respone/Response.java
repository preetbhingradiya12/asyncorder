package com.asyncorder.respone;

import lombok.*;

import java.util.Optional;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Response {
    private Number statusCode;
    private String message;
    private Optional<String> error;
}
