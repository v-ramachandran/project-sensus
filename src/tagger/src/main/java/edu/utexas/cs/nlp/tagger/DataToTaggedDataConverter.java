package edu.utexas.cs.nlp.tagger;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
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
        int i = 0;
        try(final BufferedReader bufferedReader = 
                new BufferedReader(new FileReader(sourceDataFile))) {
            
            String line;
            while((line = bufferedReader.readLine()) != null) {
                final String taggedLine = maxentTagger.tagString(line);
                final String convertedTaggedLine = 
                    convertTaggedLineTags(taggedLine);
                Files.append(convertedTaggedLine, targetDataFile, Charsets.UTF_8);
                Files.append("\n", targetDataFile, Charsets.UTF_8);
                
                System.out.println(i);
                i++;
            }
        } catch (IOException ioException) {
            throw new UnableToCreateTaggedDataFileException(ioException);
        }
    }
}
