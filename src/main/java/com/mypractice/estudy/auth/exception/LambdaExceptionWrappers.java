package com.mypractice.estudy.auth.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;


@Slf4j
public class LambdaExceptionWrappers {

    private LambdaExceptionWrappers(){}

    static <T, E extends Exception> Consumer<T> consumerWrapper(Consumer<T> consumer, Class<E> clazz) {
        return i -> {
            try {
                consumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = clazz.cast(ex);
                    log.error("Exception occurred.", exCast);
                } catch (ClassCastException ccEx) {
                    throw ex;
                }
            }
        };
    }

    public static <T> Consumer<T> throwingConsumerWrapper(ThrowingConsumer<T, Exception> throwingConsumer) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                throw new GenericException(ex.getMessage(), ex);
            }
        };
    }

    public static <T, E extends Exception> Consumer<T> handlingConsumerWrapper(ThrowingConsumer<T, E> throwingConsumer, Class<E> exceptionClass) {
        return i -> {
            try {
                throwingConsumer.accept(i);
            } catch (Exception ex) {
                try {
                    E exCast = exceptionClass.cast(ex);
                    log.error("Exception occurred.", exCast);
                } catch (ClassCastException ccEx) {
                    throw new GenericException(ex.getMessage(),ex);
                }
            }
        };
    }
}
