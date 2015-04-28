package edu.utexas.cs.nlp.tagger.exception;

public class UnsupportedPOSTagLanguageException extends RuntimeException {
    
    public UnsupportedPOSTagLanguageException(final String message) {
        super(message);
    }
}
