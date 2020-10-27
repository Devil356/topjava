package ru.javawebinar.topjava.util.exception;

import java.util.function.Supplier;

public class NotFoundException extends RuntimeException implements Supplier<String> {

    public NotFoundException(String message) {
        super(message);
    }

    @Override
    public String get() {
        return getMessage();
    }
}