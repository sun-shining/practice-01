package org.juddar.dynamicproxy;

public class AemeticImpl implements Aemetic {

    public int sub(int a, int b) {
        int result = a + b;
        return result;
    }

    public int mut(int a, int b) {
        int result = a - b;
        return result;
    }
}
