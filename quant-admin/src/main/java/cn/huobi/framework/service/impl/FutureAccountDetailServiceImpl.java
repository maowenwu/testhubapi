package cn.huobi.framework.service.impl;

import cn.huobi.framework.service.FutureAccountDetailService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.huobi.quantification.dao.QuanAccountFutureAssetMapper;
import com.huobi.quantification.dao.QuanAccountFuturePositionMapper;
import com.huobi.quantification.entity.QuanAccountFuture;
import com.huobi.quantification.entity.QuanAccountFutureAsset;
import com.huobi.quantification.entity.QuanAccountFuturePosition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service("futureAccountDetailService")
@Transactional
public class FutureAccountDetailServiceImpl implements FutureAccountDetailService {

    @Autowired
    private QuanAccountFutureAssetMapper quanAccountFutureAssetMapper;

    @Autowired
    private QuanAccountFuturePositionMapper quanAccountFuturePositionMapper;

    @Override
    public PageInfo<QuanAccountFutureAsset> selectAccountDetail(QuanAccountFuture quanAccountFuture, PageInfo<QuanAccountFutureAsset> page) {
        page.setPageSize(10000);
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        List<QuanAccountFutureAsset> quanAccountFutureAssets = quanAccountFutureAssetMapper.selectByQuanAccountFuture(quanAccountFuture);
        Map<String, List<QuanAccountFutureAsset>> collect = quanAccountFutureAssets.stream().collect(Collectors.groupingBy(e -> e.getCoinType()));
        Map<String,QuanAccountFutureAsset> assetHashMap = new HashMap<>();
        collect.keySet().stream().forEach(e -> {
            List<QuanAccountFutureAsset> accountFutureAssets = collect.get(e);
            accountFutureAssets.stream().forEach(a -> {
                if (assetHashMap.get(e)==null){
                    assetHashMap.put(a.getCoinType(),a);
                }else if (a.getCoinType().equals(assetHashMap.get(e).getCoinType())){
                    int compareNum = assetHashMap.get(e).getUpdateTime().compareTo(a.getUpdateTime());
                    if (compareNum < 0){
                        assetHashMap.put(a.getCoinType(),a);
                    }
                }
            });
        });
        List<QuanAccountFutureAsset> accountFutureAssets = new ArrayList<>();
        assetHashMap.keySet().stream().forEach(e -> accountFutureAssets.add(assetHashMap.get(e)));
        page = new PageInfo<>(accountFutureAssets);
        return page;
    }

    @Override
    public PageInfo<QuanAccountFuturePosition> selectAccountPosition(QuanAccountFuturePosition quanAccountFuturePosition, PageInfo<QuanAccountFuturePosition> page) {
        page.setPageSize(10000);
        PageHelper.startPage(page.getPageNum(),page.getPageSize());
        if (quanAccountFuturePosition.getBaseCoin() != null){
            quanAccountFuturePosition.setBaseCoin(quanAccountFuturePosition.getBaseCoin().toUpperCase());
        }
        List<QuanAccountFuturePosition> quanAccountFuturePositions = quanAccountFuturePositionMapper.selectByBaseCoin(quanAccountFuturePosition);
        Map<String, List<QuanAccountFuturePosition>> collect = quanAccountFuturePositions.stream().collect(Collectors.groupingBy(e -> e.getContractType()));
        HashMap<String, QuanAccountFuturePosition> positionHashMap = new HashMap<>();
        collect.keySet().stream().forEach(e -> {
            List<QuanAccountFuturePosition> accountFuturePositions = collect.get(e);
            accountFuturePositions.stream().forEach(a -> {
                if (positionHashMap.get(e)==null){
                    positionHashMap.put(a.getContractType(),a);
                }else if (a.getContractType().equals(positionHashMap.get(e).getContractType())){
                    int compareNum = positionHashMap.get(e).getUpdateTime().compareTo(a.getUpdateTime());
                    if (compareNum < 0){
                        positionHashMap.put(a.getContractType(),a);
                    }
                }
            });
        });
        List<QuanAccountFuturePosition> accountFuturePositions = new ArrayList<>();
        positionHashMap.keySet().stream().forEach( e -> accountFuturePositions.add(positionHashMap.get(e)));
        page = new PageInfo<>(accountFuturePositions);
        return page;
    }

}
