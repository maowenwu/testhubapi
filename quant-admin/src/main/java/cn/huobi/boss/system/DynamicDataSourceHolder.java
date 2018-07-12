package cn.huobi.boss.system;

public class DynamicDataSourceHolder {

    public final static ThreadLocal<String> holder = new ThreadLocal<>();

    public static void putDataSource(String name){
        holder.set(name);
    }

    public static String getDataSource(){
        return holder.get();
    }

    public static void clearDbType(){
        DynamicDataSourceHolder.holder.remove();
    }
}
