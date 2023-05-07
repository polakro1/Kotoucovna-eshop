package cz.example.kotoucovnaeshop.util;

import org.springframework.stereotype.Component;

import java.text.Normalizer;

@Component
public class DiacriticHandler {
    public String makeURL(String string) {
        String result = Normalizer.normalize(string, Normalizer.Form.NFD);
        result = result.replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
        result = result.replaceAll(" ", "-");
        result = result.toLowerCase();

        return result;
    }
}
