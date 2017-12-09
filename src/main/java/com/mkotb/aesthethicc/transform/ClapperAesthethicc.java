package com.mkotb.aesthethicc.transform;

import com.mkotb.aesthethicc.TextTransformer;

public class ClapperAesthethicc implements TextTransformer {
    @Override
    public String transform(String input) {
        StringBuilder builder = new StringBuilder();
        String splitter = " \uD83D\uDC4F ";

        for (String str : input.split(" ")) {
            builder.append(str);
            builder.append(splitter);
        }

        builder.setLength(builder.length() - splitter.length());
        return builder.toString();
    }

    @Override
    public String getTitle() {
        return "Clapper  \uD83D\uDC4F Aesthetic";
    }
}
