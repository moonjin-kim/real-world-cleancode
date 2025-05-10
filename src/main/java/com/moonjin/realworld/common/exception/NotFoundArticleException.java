package com.moonjin.realworld.common.exception;

public class NotFoundArticleException extends CustomException {
    private static final String MESSAGE = "존재하지 않은 기사입니다.";

    public NotFoundArticleException() { super(MESSAGE); }

    @Override
    public int getStatusCode() {
        return 404;
    }
}
