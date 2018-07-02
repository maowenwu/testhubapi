package com.huobi.quantification.service.order.impl;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.OrderHuobiService;
/**
 * @author shaoxiaofeng
 * @since 2018/6/26
 */
public class OrderHuobiServiceImpl implements OrderHuobiService {
	@Autowired
	private HttpService httpService;
	@Autowired
	private QuanOrderMapper quanOrderMapper;
	/**
     * 获取订单信息
     *
     * @return
     */
    public Object getHuobiOrderInfo() {
    	 Map<String, String> params = new HashMap<>();
         String body = httpService.okSignedPost(HttpConstant.HUOBI_ORDERDETAIL, params);
         parseAndSaveOrderInfo(body);
         return null;
    }

    /**
     * 批量获取订单信息
     *
     * @return
     */
    public Object getHuobiOrdersInfo(){
    	return null;
    }

    /**
     * 查询成交（时间顺序）
     *
     * @return
     */
    public Object getHuobiOrdersHistory(){
    	return null;
    }
    /**
     * 下单
     *
     * @return
     */
    public Object placeHuobiOrder(){
    	 Map<String, String> params = new HashMap<>();
         params.put("account-id", "");
         params.put("amount", "");
         params.put("price", "");
         params.put("source", "");
         params.put("symbol", "ethusdt");
         params.put("type", "buy-limit");
         String result = httpService.doPost(HttpConstant.HUOBI_ORDER_PLACE, params);
         return null;
    }

    /**
     * 批量下单
     *
     * @return
     */
    public Object placeHuobikOrders(){
    	return null;
    }
    /**
     * 撤单
     *
     * @return
     */
    public Object cancelHuobiOrder(){
    	return null;
    }
    /**
     * 批量撤单
     *
     * @return
     */
    public Object cancelHuobiOrders(){
    	return null;
    }
    private void parseAndSaveOrderInfo(String body) {
    	 JSONObject jsonObject = JSON.parseObject(body);
    		JSONObject jsonObjectdata=jsonObject.getJSONObject("data");
    		QuanOrder quanOrder=new QuanOrder();
    		quanOrder.setOrderState(jsonObjectdata.getString("state"));
    		//quanOrder.setOrderAmount(Long.valueOf(jsonObjectdata.getString("amount")));
    		quanOrder.setOrderSymbol(jsonObjectdata.getString("symbol"));
    		quanOrderMapper.insert(quanOrder);
    }
}
