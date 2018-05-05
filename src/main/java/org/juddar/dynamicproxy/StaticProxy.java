package org.juddar.dynamicproxy;

/**
 * 静态代理就是和被代理对象实现同一接口，对象内封装一个被代理对象，在实现接口方法内部实际调用的都是被
 * 代理对象自身的方法
 */
public class StaticProxy implements Aemetic{
    private AemeticImpl aemetic;

    public StaticProxy(AemeticImpl aemetic){
        this.aemetic = aemetic;
    }

    public int sub(int a, int b) {
        //do something before sub
        int sub = aemetic.sub(a, b);
        //do something after sub
        return sub;
    }

    public int mut(int a, int b) {
        int mut = aemetic.mut(a, b);
        return mut;
    }
}
