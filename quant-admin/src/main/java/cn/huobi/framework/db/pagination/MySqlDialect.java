package cn.huobi.framework.db.pagination;

/**
 * MySQL数据库方言
 * @author 沙
 *
 */
public class MySqlDialect extends Dialect {

    protected static final String SQL_END_DELIMITER = ";";

    @Override
    public String getLimitString(String sql, int offset, int limit) {
        return  MySqlPageHepler.getLimitString(sql, offset, limit);
    }

    @Override
    public String getCountString(String sql) {
        return  MySqlPageHepler.getCountString(sql);
    }
}
