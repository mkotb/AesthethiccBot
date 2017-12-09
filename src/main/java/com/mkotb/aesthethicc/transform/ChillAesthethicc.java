package com.mkotb.aesthethicc.transform;

public class ChillAesthethicc extends ClassicAesthethicc {
    @Override
    public String transform(String input) {
        return super.transform(input).toLowerCase();
    }

    @Override
    public String getTitle() {
        return "c h i l l  a e s t h e t i c";
    }
}
