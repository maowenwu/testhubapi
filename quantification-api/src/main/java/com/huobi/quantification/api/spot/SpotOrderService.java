package com.huobi.quantification.api.spot;

import java.util.List;
import java.util.Map;

import com.huobi.quantification.common.ServiceResult;
import com.huobi.quantification.dto.SpotActiveOrderCancelReqDto;
import com.huobi.quantification.dto.SpotBatchOrderReqDto;
import com.huobi.quantification.dto.SpotBatchOrderRespDto;
import com.huobi.quantification.dto.SpotOrderCancelReqDto;
import com.huobi.quantification.dto.SpotOrderExchangeReqDto;
import com.huobi.quantification.dto.SpotOrderInnerReqDto;
import com.huobi.quantification.dto.SpotOrderLinkReqDto;
import com.huobi.quantification.dto.SpotOrderRespDto;
import com.huobi.quantification.dto.SpotOrderStatusReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderReqDto;
import com.huobi.quantification.dto.SpotPlaceOrderRespDto;

public interface SpotOrderService {
	/**
	 * 查询订单-根据内部orderid 
	 * 正常情况下，返回结果中，一个InnerOrderID只会对应一个订单信息（唯一）
	 * @return
	 */
	ServiceResult<Map<String,Object>> getOrderByInnerOrderID(SpotOrderInnerReqDto reqDto);

	/**
	 * 查询订单-根据交易所orderid 
	 * 正常情况下，返回结果中，一个exOrderID可能会对应多个订单信息
	 * @return
	 */
	ServiceResult<Map<String,Object>> getOrderByExOrderID(SpotOrderExchangeReqDto reqDto);

	/**
	 * 查询订单-根据关联orderid
	 *正常情况下，返回结果中，一个linkOrderID可能对应多个订单（linkOrderID不唯一）
	 * @return
	 */
	ServiceResult<Map<String,Object>> getOrderByLinkOrderID(SpotOrderLinkReqDto reqDto);

	/**
	 * 查询订单-根据订单状态
	 *正常情况下，返回结果中，一个InnerOrderID只会对应一个订单信息（唯一）
	 * @return
	 */
	ServiceResult<List<SpotOrderRespDto>> getOrderByStatus(SpotOrderStatusReqDto reqDto);
	
	/**
	 * 下单接口
	 * @return
	 */
	ServiceResult<SpotPlaceOrderRespDto> placeOrder(SpotPlaceOrderReqDto reqDto);
	
	/**
	 * 撤销订单-根据内部orderID
	 * @param reqDto
	 * @return
	 */
	public ServiceResult<Object> cancelOrder(SpotOrderCancelReqDto reqDto);
	
	/**
	 * 撤销活跃订单
	 * @param reqDto
	 * @return
	 */
	public ServiceResult<Object> cancelOrder(SpotActiveOrderCancelReqDto reqDto);
	
	/**
	 * 批量下单
	 * @param reqDto
	 * @return
	 */
	public ServiceResult<List<SpotBatchOrderRespDto>> placeBatchOrders(SpotBatchOrderReqDto reqDto);
}
