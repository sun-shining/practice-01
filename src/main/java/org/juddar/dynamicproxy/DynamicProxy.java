package org.juddar.dynamicproxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.Arrays;

public class DynamicProxy implements InvocationHandler{

    private Aemetic aemetic ;


    public DynamicProxy(Aemetic aemetic){
        this.aemetic = aemetic;
    }
//    public Aemetic genDynamicProxy(){
//        Aemetic proxyInstance = (Aemetic) Proxy.newProxyInstance(aemetic.getClass().getClassLoader(),
//                aemetic.getClass().getInterfaces(),
//                new InvocationHandler() {
//                    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
//                        return method.invoke(proxy, args);
//                    }
//                });
//        return proxyInstance;
//    }

    public static void main(String[] args) {
        DynamicProxy dy = new DynamicProxy(new AemeticImpl());
        final Aemetic aemetic = new AemeticImpl();
        //Exception in thread "main" java.lang.ClassCastException: com.sun.proxy.$Proxy0 cannot be cast to org.juddar.dynamicproxy.Aemetic
        //第二个参数写类.class.getInterfaces 就会报上面的错
// 错误写法       Aemetic o = (Aemetic) Proxy.newProxyInstance(aemetic.getClass().getClassLoader(), Aemetic.class.getInterfaces(), dy);
//        Class<?>[] interfaces = aemetic.getClass().getInterfaces();
//        Class<?>[] interfaces1 = Aemetic.class.getInterfaces();
//        System.err.println(interfaces1.equals(interfaces)); 测试这两种写法的结果的确不相等
        // getInterfaces() 方法返回的是调用该方法的对象继承或者实现的所有接口
//        Aemetic o = (Aemetic) Proxy.newProxyInstance(aemetic.getClass().getClassLoader(), aemetic.getClass().getInterfaces(), dy);
//        System.out.println(o.mut(1,2));

        Class<?>[] interfaces = aemetic.getClass().getInterfaces();
        Class<?>[] interfaces1 = Aemetic.class.getInterfaces();
        System.err.println(Arrays.asList(interfaces));
        System.err.println(Arrays.asList(interfaces1));
        System.err.println(interfaces1.equals(interfaces));

        System.err.println(Arrays.asList(ArrayList.class.getInterfaces()));
    }


    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Object result = method.invoke(aemetic, args);

        return result;
    }
}
