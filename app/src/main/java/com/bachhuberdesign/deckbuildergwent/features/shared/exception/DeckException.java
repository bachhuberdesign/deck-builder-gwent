package com.bachhuberdesign.deckbuildergwent.features.shared.exception;

/**
 * @author Eric Bachhuber
 * @version 1.0.0
 * @since 1.0.0
 */
public class DeckException extends Exception {

    public DeckException(String message) {
        super(message);
    }

    public DeckException(String message, Throwable cause) {
        super(message, cause);
    }

}
