package com.oop.ptit.group4.shoppingweb.service.impl;

import com.oop.ptit.group4.shoppingweb.constants.ErrorMessage;
import com.oop.ptit.group4.shoppingweb.constants.SuccessMessage;
import com.oop.ptit.group4.shoppingweb.domain.User;
import com.oop.ptit.group4.shoppingweb.dto.request.PasswordResetRequest;
import com.oop.ptit.group4.shoppingweb.dto.response.MessageResponse;
import com.oop.ptit.group4.shoppingweb.repository.UserRepository;
import com.oop.ptit.group4.shoppingweb.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @Override
    @Transactional
    public MessageResponse sendPasswordResetCode(String email) {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            return new MessageResponse("alert-danger",  ErrorMessage.EMAIL_NOT_FOUND);
        }
        user.setPasswordResetCode(UUID.randomUUID().toString());
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("firstName", user.getFirstName());
        attributes.put("resetCode", "/auth/reset/" + user.getPasswordResetCode());
        mailService.sendMessageHtml(user.getEmail(), "Đặt lại mật khẩu", "password-reset-template", attributes);
        return new MessageResponse("alert-success",  SuccessMessage.PASSWORD_CODE_SEND);
    }

    @Override
    public String getEmailByPasswordResetCode(String code) {
        return userRepository.getEmailByPasswordResetCode(code)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, ErrorMessage.INVALID_PASSWORD_CODE));
    }

    @Override
    @Transactional
    public MessageResponse resetPassword(PasswordResetRequest request) {
        if (request.getPassword() != null && !request.getPassword().equals(request.getPassword2())) {
            return new MessageResponse("passwordError", ErrorMessage.PASSWORDS_DO_NOT_MATCH);
        }
        User user = userRepository.findByEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setPasswordResetCode(null);
        return new MessageResponse("alert-success", SuccessMessage.PASSWORD_CHANGED);
    }
}
