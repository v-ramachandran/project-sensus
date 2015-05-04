package edu.utexas.cs.nlp.tagger;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.StringJoiner;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;
import edu.utexas.cs.nlp.tagger.exception.UnableToCreateTaggedDataFileException;
import edu.utexas.cs.nlp.tagger.universal.TokenPOSTagConverter;

public class DataToTaggedDataConverter {
    
    private TokenPOSTagConverter tokenPOSTagConverter;
    private MaxentTagger maxentTagger;
    
    public DataToTaggedDataConverter(final MaxentTagger maxentTagger,
        final TokenPOSTagConverter tokenPOSTagConverter) {
        
        this.maxentTagger = maxentTagger;
        this.tokenPOSTagConverter = tokenPOSTagConverter;
    }
    
    private String convertTaggedLineTags(final String taggedLine) {
        
        final StringJoiner stringJoiner = new StringJoiner(" ");
        for (final String token : taggedLine.split("\\s+")) {
            final String convertedToken = tokenPOSTagConverter.
                convertTokenPOSTag(token);
            stringJoiner.add(convertedToken);
        }
        
        return stringJoiner.toString();
    }
    
    public void generateTaggedData(final Path sourceDataFilePath, 
        final Path targetDataFilePath) {
        
        final File sourceDataFile = sourceDataFilePath.toFile();
        final File targetDataFile = targetDataFilePath.toFile();
        if (targetDataFile.exists()) {
            targetDataFile.delete();
        }
        
        try {
            Files.touch(targetDataFile);
        } catch (IOException ioException) {
            
            throw new UnableToCreateTaggedDataFileException(ioException);
        }

        try (final BufferedWriter fileWriter = 
            Files.newWriter(targetDataFile, Charsets.UTF_8);) {
            
            for(final String line : Files.readLines(
                sourceDataFile, Charsets.UTF_8)) {
                        
                final String taggedLine = maxentTagger.tagString(line);
                final String convertedTaggedLine = 
                    convertTaggedLineTags(taggedLine);
                fileWriter.write(convertedTaggedLine);
                fileWriter.newLine();
                
            }
        } catch (IOException ioException) {
            throw new UnableToCreateTaggedDataFileException(ioException);
        }
    }
}
