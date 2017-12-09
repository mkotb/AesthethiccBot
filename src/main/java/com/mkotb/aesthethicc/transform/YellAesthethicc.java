package com.mkotb.aesthethicc.transform;

public class YellAesthethicc extends ClassicAesthethicc {
    @Override
    public String transform(String input) {
        return super.transform(input).toUpperCase();
    }

    @Override
    public String getTitle() {
        return "Y E L L  A E S T H E T I C";
    }
}
