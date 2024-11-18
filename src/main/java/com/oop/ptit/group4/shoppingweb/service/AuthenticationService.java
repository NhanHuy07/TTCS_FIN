package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.dto.request.PasswordResetRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;

public interface AuthenticationService {

    MessageResponse sendPasswordResetCode(String email);

    String getEmailByPasswordResetCode(String code);

    MessageResponse resetPassword(PasswordResetRequest request);
}
