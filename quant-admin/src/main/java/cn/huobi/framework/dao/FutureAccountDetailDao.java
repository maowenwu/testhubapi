package cn.huobi.framework.dao;

import com.huobi.quantification.entity.QuanAccountFutureAsset;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface FutureAccountDetailDao {

    @Select("select * from quan_account_future_asset where account_future_id = #{accountId} order by update_time desc limit 1")
    List<QuanAccountFutureAsset> selectAccountDetail(@Param("accountId") Integer accountId);
}
