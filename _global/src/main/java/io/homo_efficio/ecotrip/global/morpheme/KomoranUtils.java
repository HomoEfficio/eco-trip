package io.homo_efficio.ecotrip.global.morpheme;

import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;

import java.util.List;

/**
 * @author homo.efficio@gmail.com
 * created on 2020-03-31
 */
public class KomoranUtils {

    private static Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);

    public static List<Token> getMorphemes(String input) {
        return komoran.analyze(input).getTokenList();
    }

    public enum POS {
        NNP,
    }
}
