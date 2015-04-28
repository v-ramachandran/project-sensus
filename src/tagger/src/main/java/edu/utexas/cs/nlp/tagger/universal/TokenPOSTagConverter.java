package edu.utexas.cs.nlp.tagger.universal;

public interface TokenPOSTagConverter {
    
    public static String DEFAULT_TAG = "X";
    public static String DELIMITER = "_";
    
    public String convertTokenPOSTag(final String tokenWithPosTag);
}
