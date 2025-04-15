package com.moonjin.realworld.common.exception;

public class UserNotFoundException extends CustomException {
    private static final String MESSAGE = "존재하지 않은 유저입니다.";

    public UserNotFoundException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
