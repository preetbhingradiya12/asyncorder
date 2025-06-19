package com.asyncorder.service.abstactlayer;

import java.util.UUID;

public interface TokenService {
    public void saveToken(UUID userId, String jti);
}
