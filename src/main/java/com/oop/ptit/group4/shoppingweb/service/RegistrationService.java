package com.oop.ptit.group4.shoppingweb.service;

import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;
import com.oop.ptit.group4.shoppingweb.dto.request.UserRequest;

public interface RegistrationService {

    MessageResponse registration(String captchaResponse, UserRequest user);

    MessageResponse activateEmailCode(String code);
}
