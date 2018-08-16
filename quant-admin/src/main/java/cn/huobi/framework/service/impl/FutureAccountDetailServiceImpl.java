package cn.huobi.framework.service.impl;

import cn.huobi.framework.dao.FutureAccountDao;
import cn.huobi.framework.dao.FutureAccountDetailDao;
import cn.huobi.framework.service.FutureAccountDetailService;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service("futureAccountDetailService")
@Transactional
public class FutureAccountDetailServiceImpl implements FutureAccountDetailService {

    @Autowired
    private FutureAccountDao futureAccountDao;

    @Autowired
    private FutureAccountDetailDao futureAccountDetailDao;

    @Override
    public QuanAccountFuture selectAccountById(int id) {
        return futureAccountDao.selectByAccountId(id);
    }

    @Override
    public List<QuanAccountFutureAsset> selectAccountDetail(Integer accountId) {
        List<QuanAccountFutureAsset> quanAccountFutureAssets = futureAccountDetailDao.selectAccountDetail(accountId);
        return null;
    }
}
