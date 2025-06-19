package com.asyncorder.service.Impl;

import com.asyncorder.dto.LoginDTO;
import com.asyncorder.dto.LoginResponseDTO;
import com.asyncorder.dto.UserDTO;
import com.asyncorder.entity.Token;
import com.asyncorder.entity.User;
import com.asyncorder.jwtutil.CustomPrincipal;
import com.asyncorder.repository.TokenRepository;
import com.asyncorder.respone.Response;
import com.asyncorder.respone.getUserResponse;
import com.asyncorder.service.abstactlayer.AbstractUserService;
import com.asyncorder.service.abstactlayer.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends AbstractUserService implements UserService {

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private TokenServiceImpl tokenService;

    @Override
    public User registerUser(UserDTO userDTO) {
        validateEmailNotExists(userDTO.getEmail());
        User user = User.builder()
                .name(userDTO.getName())
                .email(userDTO.getEmail())
                .role(userDTO.getRole())
                .password(userDTO.getPassword())
                .build();
        return userRepository.save(hashPassword(user));
    }

    @Override
    public LoginResponseDTO loginUser(LoginDTO loginDTO) {
       User user = getUserByEmailOrThrow(loginDTO.getEmail());

       if (!passwordEncoder.matches(loginDTO.getPassword(), user.getPassword())) throw new RuntimeException("Invalid email or password");

        UUID jti = UUID.randomUUID();
        String accessToken = jwtAuthService.generateToken(user, jti);
        String refreshToken = jwtAuthService.generateRefreshToken(user, jti);

        tokenService.saveToken(user.getId(), jti.toString());

        return new LoginResponseDTO("Login successfully", accessToken, refreshToken);
    }

    @Override
    public getUserResponse getUserDetail(CustomPrincipal token) {
        CustomPrincipal user = token;
        UUID userId = (UUID) user.getUserId();
        User userData = getUserByIdOrThrow(userId);
        return new getUserResponse(200, "User data fetch sucessfully", userData);
    }

    @Override
    public Response logoutUser(CustomPrincipal token) {
        CustomPrincipal user = token;
        UUID userId = (UUID) user.getUserId();

        //Find user
        getUserByIdOrThrow(userId);

        //find token base on user
        Token tokenData = getUserToken(user.getJti());

        tokenData.setRevoked(true);
        tokenRepository.save(tokenData);

        return new Response(200, "User logout sucessfully", null);
    }

}
