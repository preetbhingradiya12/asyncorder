package com.asyncorder.service.abstactlayer;

import com.asyncorder.dto.LoginDTO;
import com.asyncorder.dto.LoginResponseDTO;
import com.asyncorder.dto.UserDTO;
import com.asyncorder.entity.User;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.respone.Response;
import com.asyncorder.respone.getUserResponse;


public interface UserService {
    User registerUser(UserDTO userDTO);
    LoginResponseDTO loginUser(LoginDTO loginDTO);
    getUserResponse getUserDetail(CustomPrincipal token);
    Response logoutUser(CustomPrincipal user);
}
