package com.huobi.quantification.provider;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.huobi.quantification.api.spot.SpotOrderService;
import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dao.QuanOrderMapper;
import com.huobi.quantification.dto.SpotOrderReqExchangeDto;
import com.huobi.quantification.dto.SpotOrderReqInnerDto;
import com.huobi.quantification.dto.SpotOrderReqLinkDto;
import com.huobi.quantification.dto.SpotOrderReqStatusDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.entity.QuanOrder;
import com.huobi.quantification.enums.ServiceErrorEnum;
import com.xiaoleilu.hutool.util.ArrayUtil;
import com.xiaoleilu.hutool.util.CollectionUtil;

@Service
public class SpotOrderServiceImpl implements SpotOrderService {

	@Autowired
	private QuanOrderMapper quanOrderMapper;

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByInnerOrderID(SpotOrderReqInnerDto reqDto) {
		List<SpotOrderRespDto> resultList = new ArrayList<>();
		ServiceResult<List<SpotOrderRespDto>> serviceResult = new ServiceResult<>();
		QuanOrder entity = new QuanOrder();
		entity.setExchangeId(reqDto.getExchangeID());
		entity.setOrderAccountId(reqDto.getAccountID());
		for (int i = 0; ArrayUtil.isNotEmpty(reqDto.getInnerOrderID()) && i < reqDto.getInnerOrderID().length; i++) {
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
		return null;
	}

	@Override
	public ServiceResult<List<SpotOrderRespDto>> getOrderByLinkOrderID(SpotOrderReqLinkDto reqDto) {
		return null;
	}

	@Override
	public ServiceResult<SpotOrderRespDto> getOrderByStatus(SpotOrderReqStatusDto reqDto) {
		return null;
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

}
