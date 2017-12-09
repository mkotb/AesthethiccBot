package com.mkotb.aesthethicc.transform;

import com.mkotb.aesthethicc.TextTransformer;

public class ClassicAesthethicc implements TextTransformer {
    @Override
    public String transform(String input) {
        StringBuilder builder = new StringBuilder();

        for (char c : input.toCharArray()) {
            builder.append(c).append(' ');
        }

        builder.setLength(builder.length() - 1);
        return builder.toString();
    }

    @Override
    public String getTitle() {
        return "C l a s s i c  A e s t h e t i c";
    }
}
