package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.common.constant.HttpConstant;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.SpotOrderReqCancelDto;
import com.huobi.quantification.dto.SpotOrderReqExchangeDto;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderReqLinkDto;
import com.huobi.quantification.dto.SpotOrderReqStatusDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.huobi.quantification.service.http.HttpService;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {

	@Autowired
	private QuanOrderMapper quanOrderMapper;
	

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
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ServiceResult<Map<String, Object>> cancelOrder(SpotOrderReqCancelDto reqDto) {
        Map<String, String> params = new HashMap<>();
        params.put("path", "11111");
        String body = httpService.doHuobiPost(HttpConstant.HUOBI_SUBMITCANCEL.replaceAll("\\{order-id\\}", "11111"), params);
        System.out.println("=========="+body);
		return null;
	}

}
