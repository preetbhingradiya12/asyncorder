package com.asyncorder.service.abstactlayer;

import com.asyncorder.dto.LoginDTO;
import com.asyncorder.dto.LoginResponseDTO;
import com.asyncorder.dto.UserDTO;
import com.asyncorder.entity.User;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.respone.Response;
import com.asyncorder.respone.getUserResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;


public interface UserService {
    User registerUser(UserDTO userDTO);
    LoginResponseDTO loginUser(LoginDTO loginDTO);

    @Cacheable(value = "userDetails", key = "#token.userId")
    getUserResponse getUserDetail(CustomPrincipal token);

    @CacheEvict(value = "userDetails", key = "#token.userId")
    getUserResponse updateUserDetail(CustomPrincipal token, UserDTO.UpdateDTO user);
    Response logoutUser(CustomPrincipal user);
}
