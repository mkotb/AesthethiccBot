package com.mkotb.aesthethicc.transform;

import com.mkotb.aesthethicc.TextTransformer;

import java.util.concurrent.ThreadLocalRandom;

public class MockingAesthethicc implements TextTransformer {
    @Override
    public String transform(String input) {
        if (input.isEmpty()) {
            return "";
        }

        StringBuilder mocked = new StringBuilder();
        char[] chars = input.toCharArray();

        mocked.append(Character.toLowerCase(chars[0]));

        for (int i = 1; i < chars.length; i++) {
            char original = chars[i];

            if (ThreadLocalRandom.current().nextInt(100) > 50) {
                original = Character.toUpperCase(original);
            } else {
                original = Character.toLowerCase(original);
            }

            mocked.append(original);
        }

        return mocked.toString();
    }

    @Override
    public String getTitle() {
        return transform("Mocking Aesthetic");
    }
}
