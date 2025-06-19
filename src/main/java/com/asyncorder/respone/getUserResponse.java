package com.asyncorder.respone;

import com.asyncorder.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class getUserResponse {
    private Number statusCode;
    private String message;
    private User data;
}
