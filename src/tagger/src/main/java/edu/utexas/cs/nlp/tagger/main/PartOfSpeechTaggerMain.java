package edu.utexas.cs.nlp.tagger.main;

import java.nio.file.Path;
import java.nio.file.Paths;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.utexas.cs.nlp.tagger.DataToTaggedDataConverter;
import edu.utexas.cs.nlp.tagger.TokenUniversalPOSTagConverterFactory;
import edu.utexas.cs.nlp.tagger.SupportedPOSTagLanguage;
import edu.utexas.cs.nlp.tagger.universal.IdentityTokenPOSTagMapper;
import edu.utexas.cs.nlp.tagger.universal.TokenPOSTagConverter;

public class PartOfSpeechTaggerMain {
    
    private static DataToTaggedDataConverter createDataToTaggedDataConverter(
        final SupportedPOSTagLanguage language,
        final boolean useUniversalTags) {
        
        TokenPOSTagConverter tokenPOSTagConverter;
        if (useUniversalTags) {
            tokenPOSTagConverter =
                new TokenUniversalPOSTagConverterFactory().createFor(language);
        } else {
            tokenPOSTagConverter = new IdentityTokenPOSTagMapper();
        }
        
        final MaxentTagger maxentTagger = new MaxentTagger(
            language.getTaggerModelFilePath());
        
        return new DataToTaggedDataConverter(maxentTagger, tokenPOSTagConverter);
    }
    
    public static void main(final String[] args) {
        
        // Normally, this might be a bit unsafe, but since this is being
        // built with Gradle, this order can be assumed/guaranteed.
        final SupportedPOSTagLanguage language = SupportedPOSTagLanguage.
            retrieveIfSupported(args[0]);
        final Path sourceDataFilePath = Paths.get(args[1]);
        final Path targetDataFilePath = Paths.get(args[2]);
        final boolean canUseUniversalTags = Boolean.valueOf(args[3]);
        
        final DataToTaggedDataConverter dataToTaggedDataConverter =
            createDataToTaggedDataConverter(language, canUseUniversalTags);
        dataToTaggedDataConverter.generateTaggedData(sourceDataFilePath, targetDataFilePath);
    }
}
