package org.example.authservice.util;

import org.example.authservice.dto.Response;
import org.springframework.http.HttpStatus;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Author: Simeon Popov
 * Date of creation: 5/13/2024
 */
public class ResponseValidatorUtil<T extends Response> {
    private final T response;

    public ResponseValidatorUtil(T response) {
        this.response = response;
    }

    public static <T extends Response> ResponseValidatorUtil<T> of(T response) {
        return new ResponseValidatorUtil<>(Objects.requireNonNull(response));
    }

    public T get() {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        return response;
    }

    public boolean isEmpty() {
        return response == null;
    }

    public ResponseValidatorUtil<T> getOnStatus(HttpStatus status) {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        if (!response
                .getHttpStatus()
                .equals(status)) {

            return ResponseValidatorUtil.of(null);
        }

        return this;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw exceptionSupplier.get();
        }

        return response;
    }

    public <X extends Throwable> ResponseValidatorUtil<T> onStatusThrow(HttpStatus.Series series, Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        if (response
                .getHttpStatus()
                .series()
                .equals(series)) {
            throw exceptionSupplier.get();
        }

        return this;
    }

    public <X extends Throwable> ResponseValidatorUtil<T> onStatusThrow(HttpStatus status, Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        if (response
                .getHttpStatus()
                .equals(status)) {
            throw exceptionSupplier.get();
        }

        return this;
    }

    public ResponseValidatorUtil<T> ifPresent() {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        return this;
    }

    public <X extends Throwable> ResponseValidatorUtil<T> anyOtherThrow(HttpStatus.Series series, Supplier<? extends X> exceptionSupplier) throws X {
        if (isEmpty()) {
            throw new NoSuchElementException("No response present");
        }

        if (response
                .getHttpStatus()
                .series()
                .equals(series)) {
            return this;
        }

        throw exceptionSupplier.get();
    }
}
