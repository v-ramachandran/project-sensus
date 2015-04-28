package edu.utexas.cs.nlp.tagger;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.opencsv.CSVReader;

import edu.utexas.cs.nlp.tagger.exception.UnableToFindPOSTagMapFileException;
import edu.utexas.cs.nlp.tagger.universal.TokenPOSTagConverter;
import edu.utexas.cs.nlp.tagger.universal.MapBasedTokenPOSTagConverter;

public class TokenUniversalPOSTagConverterFactory {
    
    private Map<String, String> createMappingFromFile(final String mapFileName) 
        throws UnableToFindPOSTagMapFileException {
        
        final Map<String, String> posTagMap = new HashMap<String, String>();
        
        try (final FileReader mapFileReader = new FileReader(mapFileName);
             final CSVReader reader = new CSVReader(mapFileReader,'\t');) {
            
            String [] nextLine;
            while ((nextLine = reader.readNext()) != null) {
                posTagMap.put(nextLine[0], nextLine[1]);
            }
            
        } catch (IOException ioException) {
            throw new UnableToFindPOSTagMapFileException(ioException);
        }
        
        return posTagMap;
    }
    
    public TokenPOSTagConverter createFor(final SupportedPOSTagLanguage language) {
        
        final String universalMapperFilePath = language.
            getUniversalTagMapperFilePath();
        final Map<String, String> posMapping = createMappingFromFile(
            universalMapperFilePath);
        return new MapBasedTokenPOSTagConverter(posMapping);
    }
}
