package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.common.util.AsyncUtils;
import com.huobi.quantification.dao.QuanOrderMapper;
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
import com.xiaoleilu.hutool.json.JSONObject;
import com.xiaoleilu.hutool.json.JSONUtil;
import com.xiaoleilu.hutool.util.ArrayUtil;

import io.netty.util.internal.StringUtil;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private QuanOrderMapper quanOrderMapper;

	@Autowired
	private HuobiOrderService huobiOrderService;

	@Autowired
	private HttpService httpService;

	@Override
	public ServiceResult<Map<String, Object>> getOrderByInnerOrderID(SpotOrderReqInnerDto reqDto) {
		ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
		Map<String, Object> map = new HashMap<>();
		QuanOrder entity = new QuanOrder();
		Integer exchangeID = reqDto.getExchangeID();
		Long accountID = reqDto.getAccountID();
		Long[] innerOrderIDs = reqDto.getInnerOrderID();
		if (null == exchangeID || null == accountID || ArrayUtil.isEmpty(innerOrderIDs)) {
			serviceResult.setCode(ServiceErrorEnum.PARAM_MISS.getCode());
			serviceResult.setMessage(ServiceErrorEnum.PARAM_MISS.getMessage());
			serviceResult.setData(null);
			return serviceResult;
		}
		entity.setExchangeId(reqDto.getExchangeID());
		entity.setOrderAccountId(reqDto.getAccountID());
		for (int i = 0; ArrayUtil.isNotEmpty(reqDto.getInnerOrderID()) && i < reqDto.getInnerOrderID().length; i++) {
			// 根据三个条件查询订单 ,一个InnerOrderID只会对应一个订单信息（唯一）
			entity.setId(reqDto.getInnerOrderID()[i]);
			List<QuanOrder> list = quanOrderMapper.selectList(entity);
			List<SpotOrderRespDto> result = copySpotOrderListToSpotOrderRespLIstDto(list);
			map.put(String.valueOf(reqDto.getInnerOrderID()[i]), result);
		}
		Map<String, Object> resultMap = new HashMap<>();
		resultMap.put("status", "ok");
		resultMap.put("data", map);
		serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		serviceResult.setData(resultMap);
		return serviceResult;
	}

	@Override
	public ServiceResult<Map<String, Object>> getOrderByExOrderID(SpotOrderReqExchangeDto reqDto) {
		Map<String, Object> map = new HashMap<>();
		ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		Integer exchangeID = reqDto.getExchangeID();
		Long accountID = reqDto.getAccountID();
		Long[] exOrderIDs = reqDto.getExOrderID();
		if (null == exchangeID || null == accountID || ArrayUtil.isEmpty(exOrderIDs)) {
			serviceResult.setCode(ServiceErrorEnum.PARAM_MISS.getCode());
			serviceResult.setMessage(ServiceErrorEnum.PARAM_MISS.getMessage());
			serviceResult.setData(null);
			return serviceResult;
		}
		entity.setExchangeId(exchangeID);
		entity.setOrderAccountId(accountID);
		for (int i = 0; i < reqDto.getExOrderID().length; i++) {
			// 根据三个条件查询订单 ,一个exOrderID可能会对应多个订单信息
			entity.setOrderSourceId(reqDto.getExOrderID()[i]);
			List<QuanOrder> list = quanOrderMapper.selectList(entity);
			List<SpotOrderRespDto> result = copySpotOrderListToSpotOrderRespLIstDto(list);
			map.put(String.valueOf(reqDto.getExOrderID()[i]), result);
		}
		serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
		serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
		serviceResult.setData(map);
		return serviceResult;
	}

	@Override
	public ServiceResult<Map<String,Object>> getOrderByLinkOrderID(SpotOrderReqLinkDto reqDto) {
		return null;
	}

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByStatus(SpotOrderReqStatusDto reqDto) {
		ServiceResult<List<SpotOrderRespDto>> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		Integer exchangeID = reqDto.getExchangeID();
		Long accountID = reqDto.getAccountID();
		String status=reqDto.getStatus();
		if (null == exchangeID || null == accountID ||StringUtil.isNullOrEmpty(status)) {
			serviceResult.setCode(ServiceErrorEnum.PARAM_MISS.getCode());
			serviceResult.setMessage(ServiceErrorEnum.PARAM_MISS.getMessage());
			serviceResult.setData(null);
			return serviceResult;
		}
		entity.setExchangeId(exchangeID);
		entity.setOrderAccountId(accountID);
		entity.setOrderState(status);
		// 根据三个条件查询订单 ,正常情况下，返回结果中，一个InnerOrderID只会对应一个订单信息（唯一）
		List<QuanOrder> list = quanOrderMapper.selectList(entity);
		List<SpotOrderRespDto> result = copySpotOrderListToSpotOrderRespLIstDto(list);
		serviceResult.setData(result);
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
		if (reqDto.getSide().equals("buy") || reqDto.getSide().equals("sell")) {
			if (reqDto.getOrderType().contains("market")) {
				orderDto.setAmount(reqDto.getCashAmount());
			} else {
				orderDto.setPrice(reqDto.getPrice());
				orderDto.setAmount(reqDto.getQuantity());
			}
		}else {
			new RuntimeException(ServiceErrorEnum.PARAM_ERROR.getMessage());
		}
		orderDto.setSource("api");
		String symbol = getSymbol(reqDto.getExchangeId(), reqDto.getBaseCoin(), reqDto.getQuoteCoin());
		orderDto.setSymbol(symbol);
		orderDto.setType(reqDto.getSide() + "-" +reqDto.getOrderType());
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
			} catch (ExecutionException e) {
				e.printStackTrace();
				serviceResult.setMessage(ServiceErrorEnum.EXECUTION_ERROR.getMessage());
				serviceResult.setCode(ServiceErrorEnum.EXECUTION_ERROR.getCode());
			}
		}
		quanOrder.setOrderState("submitting");
		quanOrder.setOrderSymbol(symbol);
		quanOrder.setOrderType(reqDto.getSide() + "-" +reqDto.getOrderType());
		quanOrder.setOrderAmount(orderDto.getAmount());
		quanOrder.setOrderPrice(reqDto.getPrice());
		quanOrder.setOrderSource("api");
		quanOrder.setOrderCreatedAt(new Date());
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
	public ServiceResult<Map<String, Object>> cancelOrder(SpotOrderReqCancelDto reqDto) {
		ServiceResult<Map<String, Object>> serviceResult = new ServiceResult<>();
		Boolean status = true;
		List<Long> successList = new ArrayList<>();
		List<Map<String, Object>> failList = new ArrayList<>();
		Map<String, Object> resultMap = new HashMap<>();
		String errorCode = "";
		List<Long> orderIds = new ArrayList<>();
		// 根据exchangeID accountID orders查询orderId
		List<Orders> orderList = reqDto.getOrders();
		for (int i = 0; i < orderList.size(); i++) {
			JSONObject json = JSONUtil.parseObj(orderList.get(i));
			QuanOrder entity = new QuanOrder();
			Long innerOrderID = json.getLong("innerOrderID");
			Long exOrderID = json.getLong("exOrderID");
			if (null == innerOrderID && null == exOrderID) {// 三种orderID至少填写一种 ,linkId待定
				continue;
			}
			entity.setId(innerOrderID);
			entity.setOrderSourceId(exOrderID);
			entity.setExchangeId(reqDto.getExchangeID());
			entity.setOrderAccountId(reqDto.getAccountID());
			List<QuanOrder> resultList = quanOrderMapper.selectList(entity);
			for (QuanOrder temp : resultList) {
				orderIds.add(temp.getOrderSourceId());
			}
		}
		com.alibaba.fastjson.JSONObject parseObject = null;
		if (reqDto.isSync()) {// 暂时只处理同步
			if (!reqDto.isParallel()) {// 单个撤销订单
				for (Long tempOrderId : orderIds) {
					String body = "";
					try {
						body = httpService.doHuobiPost(reqDto.getAccountID(), HttpConstant.HUOBI_SUBMITCANCEL
								.replaceAll("\\{order-id\\}", String.valueOf(tempOrderId)), null);
						Thread.sleep(reqDto.getTimeInterval() == null ? 100L : reqDto.getTimeInterval());
						logger.info("订单号:{}取消订单返回的结果为:{}", String.valueOf(tempOrderId), body);
						parseObject = JSON.parseObject(body);
						errorCode = parseObject.getString("err-code");
					} catch (Exception e) {
						logger.error("订单号:{}取消订单发生了异常:{}", String.valueOf(tempOrderId), e);
						errorCode = String.valueOf(ServiceErrorEnum.TIMEOUT_ERROR.getCode());
					}
					if (null != parseObject && "ok".equalsIgnoreCase(parseObject.getString("status"))) {
						successList.add(tempOrderId);
					} else {
						Map<String, Object> failMap = new HashMap<>();
						failMap.put("innerOrderID", String.valueOf(tempOrderId));
						failMap.put("error_code", errorCode);
						status = false;
						failList.add(failMap);
					}
				}
				resultMap.put("success", successList);
				resultMap.put("fail", failList);
				serviceResult.setData(resultMap);
			} else {
				// 调用批量撤销接口单次不超过50个订单id
				Map<String, Object> paramMap = new HashMap<>();
				List<String> list = new ArrayList<>();
				for (Long temp : orderIds) {
					list.add(temp.toString());
				}
				for (int i = 0; 50 * i <= list.size(); i++) {
					paramMap.put("order-ids", list.subList(50 * i, 50));
					logger.info("批量取消订单的请求orderIds为:{}", list.subList(50 * i, 50));
					try {
						String body = httpService.doHuobiPost(reqDto.getAccountID(), HttpConstant.HUOBI_BATCHCANCEL,
								paramMap);
						logger.info("批量取消订单返回的结果为:{}", body);
						resultMap = parseData(body);
					} catch (Exception e) {
						logger.error("批量取消订单发生了异常:{}", e);
						status = false;
					}
				}

			}
			if (status) {
				resultMap.put("status", "ok");
			}
			serviceResult.setCode(ServiceErrorEnum.SUCCESS.getCode());
			serviceResult.setMessage(ServiceErrorEnum.SUCCESS.getMessage());
			serviceResult.setData(resultMap);
		}
		return serviceResult;
	}

	/**
	 * 解析批量撤单返回的数据
	 * 
	 * @param input input
	 * @return Map<String, Object>
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Map<String, Object> parseData(String input) {
		Map<String, Object> resultMap = new HashMap<>();
		Map maps = (Map) JSON.parse(input);
		Object object = maps.get("data");
		Map<String, Object> objectMap = (Map<String, Object>) JSON.parse(object.toString());
		List successMap = (List) JSON.parse(objectMap.get("success").toString());
		List<Map> failMap = (List<Map>) JSON.parse(objectMap.get("failed").toString());
		for (Map tempMap : failMap) {
			tempMap.put("innerOrderID", tempMap.get("order-id"));
			tempMap.put("error_code", tempMap.get("err-code"));
			tempMap.remove("order-id");
			tempMap.remove("err-code");
			tempMap.remove("err-msg");
		}
		resultMap.put("success", successMap);
		resultMap.put("fail", failMap);
		return resultMap;
	}

}
