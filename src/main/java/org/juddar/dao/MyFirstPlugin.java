package org.juddar.dao;

import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.SqlSource;
import org.apache.ibatis.plugin.*;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;

import java.util.Properties;

@Intercepts({
        @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class})//告诉插件拦截哪个对象的哪个方法
})
public class MyFirstPlugin implements Interceptor {
    static int MAPPED_STATEMENT_INDEX = 0;// 这是对应上面的args的序号
    static int PARAMETER_INDEX = 1;
    static int ROWBOUNDS_INDEX = 2;
    static int RESULT_HANDLER_INDEX = 3;

    public Object intercept(Invocation invocation) throws Throwable {
        System.out.println("MyFirstPlugin..."+invocation.getMethod());

        //拿到PreparedStatement里的 parameterHandler里的parameterObject
        Object[] queryArgs = invocation.getArgs();
        final MappedStatement mappedStatement = (MappedStatement) queryArgs[MAPPED_STATEMENT_INDEX];
        final Object parameter = queryArgs[PARAMETER_INDEX];
        final BoundSql boundSql = mappedStatement.getBoundSql(parameter);

        String sql = boundSql.getSql();
        String replace = "";
        if (sql.contains(";")){
            replace = sql.replace(";", " where id = 1");
        }


        BoundSql boundSql1 = new BoundSql(mappedStatement.getConfiguration(), replace, boundSql.getParameterMappings(), boundSql.getParameterObject());
        MappedStatement mappedStatement1 = copyFromMappedStatement(mappedStatement, new BoundSqlSqlSource(boundSql1));
        for (ParameterMapping mapping : boundSql.getParameterMappings()) {
            String prop = mapping.getProperty();
            if (boundSql.hasAdditionalParameter(prop)) {
                boundSql1.setAdditionalParameter(prop, boundSql.getAdditionalParameter(prop));
            }
        }
        queryArgs[MAPPED_STATEMENT_INDEX] = mappedStatement1;
        Object proceed = invocation.proceed();
        return proceed;
    }

    public static class BoundSqlSqlSource implements SqlSource {
        private BoundSql boundSql;
        public BoundSqlSqlSource(BoundSql boundSql) {
            this.boundSql = boundSql;
        }
        public BoundSql getBoundSql(Object parameterObject) {
            return boundSql;
        }
    }

    private MappedStatement copyFromMappedStatement(MappedStatement ms, SqlSource newSqlSource) {
        MappedStatement.Builder builder = new MappedStatement.Builder(ms.getConfiguration(), ms.getId(), newSqlSource, ms.getSqlCommandType());
        builder.resource(ms.getResource());
        builder.fetchSize(ms.getFetchSize());
        builder.statementType(ms.getStatementType());
        builder.keyGenerator(ms.getKeyGenerator());
        if (ms.getKeyProperties() != null && ms.getKeyProperties().length > 0) {
            builder.keyProperty(ms.getKeyProperties()[0]);
        }
        builder.timeout(ms.getTimeout());
        builder.parameterMap(ms.getParameterMap());
        builder.resultMaps(ms.getResultMaps());
        builder.resultSetType(ms.getResultSetType());
        builder.cache(ms.getCache());
        builder.flushCacheRequired(ms.isFlushCacheRequired());
        builder.useCache(ms.isUseCache());
        return builder.build();
    }

    public Object plugin(Object target) {
        System.out.println("MyfirstPlugin....intercepr"+ target);
        Object wrap = Plugin.wrap(target, this);
        return wrap;
    }

    /**
     * 将插件注册时的属性设置进来
     * @param properties
     */
    public void setProperties(Properties properties) {
        System.out.println("MyfirstPlugin....intercepr"+properties.get("username"));
    }
}
