package edu.utexas.cs.nlp.tagger.universal;

import java.util.Map;

public class MapBasedTokenPOSTagConverter implements TokenPOSTagConverter {
    
    private Map<String, String> taggingMap;
    
    public MapBasedTokenPOSTagConverter(final Map<String,String> taggingMap) {
        this.taggingMap = taggingMap;
    }
    
    @Override
    public String convertTokenPOSTag(String tokenWithPosTag) {
        
        final int lastDelimiterOccurrence = tokenWithPosTag.lastIndexOf("_");
        final String prefix = tokenWithPosTag.substring(0, lastDelimiterOccurrence);
        final String mappablePOSTag = tokenWithPosTag.substring(
            lastDelimiterOccurrence+1);
        
        final String result = taggingMap.get(mappablePOSTag);
        if (result == null) {
            return prefix + DELIMITER + DEFAULT_TAG;
        } else {
            return prefix + DELIMITER + result;
        }
    }
}
