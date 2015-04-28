package edu.utexas.cs.nlp.tagger;

import edu.utexas.cs.nlp.tagger.exception.UnsupportedPOSTagLanguageException;

public enum SupportedPOSTagLanguage {
    
    ENGLISH("models/english-left3words-distsim.tagger", "models/en-universal-pos.map"), 
    SPANISH("models/spanish-distsim.tagger","models/es-universal-pos.map");
    
    private String taggerModelFilePath;
    private String universalTagMapperFilePath;

    private SupportedPOSTagLanguage(final String taggerModelFilePath, 
        final String universalTagMapperFilePath) {
        
        this.taggerModelFilePath = taggerModelFilePath;
        this.universalTagMapperFilePath = universalTagMapperFilePath;
    }
    
    public String getTaggerModelFilePath() {
        return taggerModelFilePath;
    }
    
    public String getUniversalTagMapperFilePath() {
        return universalTagMapperFilePath;
    }
    
    public static SupportedPOSTagLanguage retrieveIfSupported(
        final String languageName) {
        
        for(final SupportedPOSTagLanguage supportedLanguage : 
            SupportedPOSTagLanguage.values()) {
            
            if (supportedLanguage.name().equals(languageName)) {
                return supportedLanguage;
            }
        }
        
        final String exceptionMessage = String.format(
            "Unable to find a SupportedPOSTagLanguage %s", languageName);
        throw new UnsupportedPOSTagLanguageException(exceptionMessage);
    }
}
