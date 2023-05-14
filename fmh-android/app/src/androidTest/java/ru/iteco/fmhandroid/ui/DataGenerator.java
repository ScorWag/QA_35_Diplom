package ru.iteco.fmhandroid.ui;

import java.security.SecureRandom;
import java.util.Random;

public class DataGenerator {
    private DataGenerator() {
    }

    private static final String cyrillicAlphabet =
            "АаБбВвГгДдЕеЖжЗзИиЙйКкЛлМмНнОоПпРрСсТтУуФфХхЦцЧчШшЩщЪъЫыЬьЭэЮюЯя";
    private static final String latinAlphabet =
            "AaBbCcDdEeFfGgHhIiJjKkLlMmNnOoPpQqRrSsTtUuVvWwXxYyZz";
    private static final String specSymbols = "`~!@#№$;%^:?&*()_-=+[]{}<>,./|";
    private static final String cyrillicValue = "ru";
    private static final String latinValue = "en";
    private static final String specSymbolsOnlyValue = "special";
    private static final String specSymbolsAndCyrillicValue = "specialRu";
    private static final String specSymbolsAndLatinValue = "specialEn";

    public static String generateSymbols(int length, String type) {
        String symbols;
        switch (type) {
            case (cyrillicValue):
                symbols = cyrillicAlphabet;
                break;
            case (latinValue):
                symbols = latinAlphabet;
                break;
            case (specSymbolsOnlyValue):
                symbols = specSymbols;
                break;
            case (specSymbolsAndCyrillicValue):
                symbols = cyrillicAlphabet + specSymbols;
                break;
            case (specSymbolsAndLatinValue):
                symbols = latinAlphabet + specSymbols;
                break;
            default:
                symbols = cyrillicAlphabet + latinAlphabet + specSymbols;
                break;
        }
        Random rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int random = rnd.nextInt(symbols.length());
            char c = symbols.charAt(random);
            sb.append(c);
        }
        return sb.toString();
    }

    public static String generateSymbols(int length) {
        String symbols = cyrillicAlphabet + latinAlphabet + specSymbols;
        Random rnd = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int random = rnd.nextInt(symbols.length());
            char c = symbols.charAt(random);
            sb.append(c);
        }
        return sb.toString();
    }
}
