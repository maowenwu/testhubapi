package tools;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;


/**
 * 快捷生成字段，与属性的映射
 * <p>Description: <／p>
 * <p>Company: ls.eeepay.cn<／p> 
 * @author liusha
 * @date 2016年5月11日
 */
public class JDBCTest {
	@Test
	public void readColumns() throws Exception{
		List<String> list=getConn();
		List<String> list1=new ArrayList();
		System.out.println(list);
		for(String str:list){
			list1.add("#'{'list[{0}]."+repl(str)+"'}'");
		}
		System.out.println(list1);
	}
	
	public List<String> getConn() throws Exception{
		List<String> list=new ArrayList();
		String url="jdbc:mysql://192.168.3.172/nposp?useUnicode=true&characterEncoding=utf8&autoReconnect=true&failOverReadOnly=false&zeroDateTimeBehavior=convertToNull";
		String user="puser";
		String pwd="gluser20156.com";
		String driver="com.mysql.jdbc.Driver";
		Class.forName(driver);
		Connection  conn = DriverManager.getConnection(url,user,pwd);
		Statement stmt = conn.createStatement();
        String sql = "SELECT column_name FROM information_schema.COLUMNS WHERE TABLE_SCHEMA ='nposp' AND table_name='service_ladder_rate'";
        ResultSet rs=stmt.executeQuery(sql);
        while(rs.next()) {
        	list.add(rs.getString("column_name"));
        }	
        conn.close();
		return list;
	}
	
	public String repl(String str){
			StringBuffer sb= new StringBuffer(str);
			int i=0;
			while((i=sb.indexOf("_"))>0){
				sb.deleteCharAt(i);
				sb.setCharAt(i, Character.toUpperCase(sb.charAt(i)));
			}
			return sb.toString();
	}
	
}
