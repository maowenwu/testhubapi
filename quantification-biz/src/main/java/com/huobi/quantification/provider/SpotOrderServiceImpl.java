package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.FutureOrderRespDto;
import com.huobi.quantification.dto.HuobiTradeOrderDto;
import com.huobi.quantification.dto.SpotOrderReqCancelDto;
import com.huobi.quantification.dto.SpotOrderReqCancelDto.Orders;
import com.huobi.quantification.dto.SpotOrderReqExchangeDto;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderReqLinkDto;
import com.huobi.quantification.dto.SpotOrderReqStatusDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ExchangeEnum;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.http.HttpService;
import com.huobi.quantification.service.order.HuobiOrderService;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {

	@Autowired
	private QuanOrderMapper quanOrderMapper;
	
	@Autowired
	private HuobiOrderService huobiOrderService;

	@Autowired
	private HttpService httpService;

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByInnerOrderID(SpotOrderReqInnerDto reqDto) {
		List<SpotOrderRespDto> resultList = new ArrayList<>();
		ServiceResult<List<SpotOrderRespDto>> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		entity.setExchangeId(reqDto.getExchangeID());
		entity.setOrderAccountId(reqDto.getAccountID());
		for (int i = 0; ArrayUtil.isNotEmpty(reqDto.getInnerOrderID()) && i < reqDto.getInnerOrderID().length; i++) {
			// 根据三个条件查询订单 ,一个InnerOrderID只会对应一个订单信息（唯一）
			entity.setInnerId(reqDto.getInnerOrderID()[i]);
			List<QuanOrder> list = quanOrderMapper.selectList(entity);
			if (CollectionUtil.isNotEmpty(list)) {
				SpotOrderRespDto result = copySpotOrderToSpotOrderRespDto(list.get(0));
				resultList.add(result);
			}
		}
		serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		serviceResult.setData(resultList);
		return serviceResult;
	}

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByExOrderID(SpotOrderReqExchangeDto reqDto) {
		List<SpotOrderRespDto> resultList = new ArrayList<>();
		ServiceResult<List<SpotOrderRespDto>> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		entity.setExchangeId(reqDto.getExchangeID());
		entity.setOrderAccountId(reqDto.getAccountID());
		for (int i = 0; ArrayUtil.isNotEmpty(reqDto.getExOrderID()) && i < reqDto.getExOrderID().length; i++) {
			// 根据三个条件查询订单 ,一个exOrderID可能会对应多个订单信息
			entity.setOrderSourceId(reqDto.getExOrderID()[i]);
			List<QuanOrder> list = quanOrderMapper.selectList(entity);
			if (CollectionUtil.isNotEmpty(list)) {
				List<SpotOrderRespDto> result = copySpotOrderListToSpotOrderRespLIstDto(list);
				resultList.addAll(result);
			}
		}
		serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		serviceResult.setData(resultList);
		return serviceResult;
	}

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByLinkOrderID(SpotOrderReqLinkDto reqDto) {
		return null;
	}

	@Override
	public ServiceResult<SpotOrderRespDto> getOrderByStatus(SpotOrderReqStatusDto reqDto) {
		ServiceResult<SpotOrderRespDto> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		entity.setExchangeId(reqDto.getExchangeID());
		entity.setOrderAccountId(reqDto.getAccountID());
		entity.setOrderState(reqDto.getStatus());
		// 根据三个条件查询订单 ,正常情况下，返回结果中，一个InnerOrderID只会对应一个订单信息（唯一）
		List<QuanOrder> list = quanOrderMapper.selectList(entity);
		if (CollectionUtil.isNotEmpty(list)) {
			SpotOrderRespDto result = copySpotOrderToSpotOrderRespDto(list.get(0));
			serviceResult.setData(result);
		}
		serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		return serviceResult;
	}

	/**
	 * 将数据库查到的实体转换为对应的响应输出结果
	 * 
	 * @param quanOrder quanOrder
	 * @return SpotOrderRespDto
	 */
	public SpotOrderRespDto copySpotOrderToSpotOrderRespDto(QuanOrder quanOrder) {
		SpotOrderRespDto result = new SpotOrderRespDto();
		result.setExOrderID(quanOrder.getExchangeId());
		// result.setLinkOrderID();
		result.setCreateTime(quanOrder.getOrderCreatedAt());
		// result.setUpdateTime();
		result.setStatus(quanOrder.getOrderState());
		// result.setBaseCoin();
		// result.setQuoteCoin();
		// result.setSide();
		result.setOrderType(quanOrder.getOrderType());
		result.setOrderPrice(quanOrder.getOrderPrice());
		result.setDealPrice(quanOrder.getOrderFieldCashAmount());
		// result.setOrderQty();
		// result.setDealQty();
		result.setOrderCashAmount(quanOrder.getOrderAmount());
		result.setDealCashAmount(quanOrder.getOrderFieldCashAmount());
		// result.setRemainingQty();
		result.setFees(quanOrder.getOrderFieldFees());
		return result;
	}

	/**
	 * 将数据库查到的实体(list)转换为对应的响应输出结果List<SpotOrderRespDto>
	 * 
	 * @param list list
	 * @return List<SpotOrderRespDto>
	 */
	public List<SpotOrderRespDto> copySpotOrderListToSpotOrderRespLIstDto(List<QuanOrder> list) {
		List<SpotOrderRespDto> result = new ArrayList<>();
		for (QuanOrder temp : list) {
			result.add(copySpotOrderToSpotOrderRespDto(temp));
		}
		return result;
	}

	@Override
	public ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto) {
		HuobiTradeOrderDto orderDto = new HuobiTradeOrderDto();
		orderDto.setAccountId(reqDto.getAccountId());
		if (reqDto.getOrderType().contains("market")) {
			orderDto.setAmount(reqDto.getCashAmount());
		}else {
			orderDto.setPrice(reqDto.getPrice());
			orderDto.setAmount(reqDto.getQuantity());
		}
		orderDto.setSource("api");
		orderDto.setSymbol(getSymbol(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getQuoteCoin()));
		orderDto.setType(reqDto.getOrderType());
		Future<Long> orderIdFuture = AsyncUtils.submit(() -> huobiOrderService.placeHuobiOrder(orderDto));
		ServiceResult<SpotPlaceOrderRespDto> serviceResult = new ServiceResult<>();
        serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
        serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
        
        SpotPlaceOrderRespDto respDto = new SpotPlaceOrderRespDto();
        QuanOrder quanOrder = new QuanOrder();
        quanOrder.setExchangeId(ExchangeEnum.HUOBI.getExId());
        quanOrder.setOrderAccountId(reqDto.getAccountId());
        if (reqDto.isSync()) {
			try {
				quanOrder.setOrderSourceId(orderIdFuture.get());
				respDto.setExOrderId(orderIdFuture.get());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}catch (ExecutionException e) {
                e.printStackTrace();
                serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
                serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
            }
		}
        quanOrderMapper.insert(quanOrder);
        respDto.setLinkOrderId(reqDto.getLinkOrderId());
        respDto.setInnerOrderId(quanOrder.getId());
        serviceResult.setData(respDto);
		return serviceResult;
	}
	
	private String getSymbol(int exchangeId, String baseCoin, String quoteCoin) {
		if (exchangeId == ExchangeEnum.HUOBI.getExId()) {
			return baseCoin.toLowerCase() + quoteCoin.toLowerCase();
		} else {
			throw new UnsupportedOperationException("交易所" + exchangeId + ",还不支持");
		}
	}
	
	@Override
	public  ServiceResult<Map<String, Object>> cancelOrder(SpotOrderReqCancelDto reqDto){
		ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
		Boolean status=true;
		List<Long>  successList=new ArrayList<>();
		List<Map<String,Object>> failList=new ArrayList<>();
		Map<String,Object> resultMap=new HashMap<>();
		String orderId="7933854403";
		//根据exchangeID accountID orders查询orderId
		List<Orders> orderList=reqDto.getOrders();
		for(int i=0;i<orderList.size();i++) {
			QuanOrder entity=new QuanOrder();
			entity.setExchangeId(reqDto.getExchangeID());
			entity.setOrderAccountId(reqDto.getAccountID());
			entity.setId(orderList.get(i).getInnerOrderID());
			entity.setOrderSourceId(orderList.get(i).getExOrderID());
			List<QuanOrder> resultList = quanOrderMapper.selectList(entity);
			for(QuanOrder temp:resultList) {
				orderId=String.valueOf(temp.getOrderSourceId());
				String body =httpService.doHuobiPost(HttpConstant.HUOBI_SUBMITCANCEL.replaceAll("\\{order-id\\}", orderId), null);
				JSONObject parseObject = JSON.parseObject(body);
				if("ok".equalsIgnoreCase(parseObject.getString("status"))) {
					successList.add(temp.getOrderSourceId());
				}else {
					Map<String, Object> failMap=new HashMap<>();
					failMap.put("innerOrderID", temp.getOrderSourceId());
					failMap.put("error_code", "????");
					status=false;
					failList.add(failMap);
				}
			}
		}
		
		if(status) {
			resultMap.put("status", "ok");
		}
		resultMap.put("success", successList);
		resultMap.put("fail", failList);
        serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		serviceResult.setData(resultMap);
		return serviceResult;
	}
	
	

	
}
