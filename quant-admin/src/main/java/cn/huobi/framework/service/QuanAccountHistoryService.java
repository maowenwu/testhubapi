package cn.huobi.framework.service;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.huobi.quantification.entity.QuanAccountHistory;

public interface QuanAccountHistoryService {
	int deleteByPrimaryKey(Long id);

	int insert(QuanAccountHistory record);

	QuanAccountHistory selectByPrimaryKey(Long id);

	List<QuanAccountHistory> selectAll();

	int updateByPrimaryKey(QuanAccountHistory record);

	// 根据
	BigDecimal getInitAmount(@Param("accountId") Long accountId, @Param("exchangeId") int exchangeId,
			@Param("coin") String coin);

}
