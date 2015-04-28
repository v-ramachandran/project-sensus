package edu.utexas.cs.nlp.tagger.universal;

public class IdentityTokenPOSTagMapper implements TokenPOSTagConverter {

    @Override
    public String convertTokenPOSTag(final String tokenWithPosTag) {
        return tokenWithPosTag;
    }
}
