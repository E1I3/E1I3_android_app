package com.example.danum.feature.utils;

import java.util.ArrayList;
import java.util.List;

public class LabelTextBuilder {
    private List<String> texts = new ArrayList<>();
    private List<Integer> textStyleIndexes = new ArrayList<>();

    public LabelTextBuilder addText(String text) {
        texts.add(text);
        return this;
    }

    public LabelTextBuilder addText(String text, int styleIndex) {
        texts.add(text);
        textStyleIndexes.add(styleIndex);
        return this;
    }

    public String[] getTexts() {
        return texts.toArray(new String[0]);
    }

    public int[] getTextIndexes() {
        int[] indexes = new int[textStyleIndexes.size()];
        for(int i = 0; i < textStyleIndexes.size(); i++) {
            indexes[i] = textStyleIndexes.get(i);
        }
        return indexes;
    }
}