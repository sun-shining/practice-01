package org.juddar.dao;

import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.plugin.*;

import java.sql.Statement;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "parameterize", args = Statement.class)//告诉插件拦截哪个对象的哪个方法
})
public class MySecondPlugin implements Interceptor {

    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MySecondPlugin...   "+invocation.getMethod());
        Object proceed = invocation.proceed();
        return proceed;
    }

    public Object plugin(Object target) {
        System.out.println("MySecondPlugin....intercepr"+ target);
        Object wrap = Plugin.wrap(target, this);
        return wrap;
    }

    /**
     * 将插件注册时的属性设置进来
     * @param properties
     */
    public void setProperties(Properties properties) {
        System.out.println("MySecondPlugin....intercepr"+properties.get("username"));
    }
}
