package cn.huobi.framework.service.impl;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * 自动生成序列,独立事务不回滚
 * @author 沙
 *
 */
@Service("SeqService")
@Transactional(isolation=Isolation.READ_UNCOMMITTED,propagation=Propagation.NOT_SUPPORTED)
public class SeqService {
	private static final Logger log = LoggerFactory.getLogger(SeqService.class);
	@Resource
	private DataSource dataSource;
	private String getKey(String table) {
		Connection conn=null;
		String num = null;
		try {
			conn=dataSource.getConnection();
			conn.setAutoCommit(true);
			PreparedStatement pst = conn.prepareStatement("select nextval(?)");
			pst.setString(1, table);
			ResultSet rs= pst.executeQuery();
			if(rs.next())
				num=rs.getString(1);
			rs.close();
			pst.close();
		} catch (Exception e) {
			log.error("产生序列失败！",e);
		}finally {
			if(conn!=null){
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return num;
	}

	/**
	 * %012d
	 * @param seqName 序列名称
	 * @param format String.format格式
	 * @return
	 */
	public String createKey(String seqName,String format){
		String num = getKey(seqName);
		return String.format(format, new BigInteger(num));
	}
	
	/**
	 * @param seqName 序列名称
	 * @param defaultValue 默认值
	 * @return @description 90000000000000000000+120000=90000000000000120000
	 */
	public String createKey(String seqName,BigInteger defaultValue){
		String num = getKey(seqName);
		BigInteger big = new BigInteger(num).add(defaultValue);
		return big.toString();
	}
	
	/**
	 * @param seqName 序列名称
	 * @return 自增长的序列，灵活格式化
	 */
	public String createKey(String seqName){
		return getKey(seqName);
	}
	
}
