package com.huobi.contract.index.monitor.impl;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.huobi.contract.index.contract.index.service.HuobiApiService;
import com.huobi.contract.index.dao.IndexInfoMapper;
import com.huobi.contract.index.dao.QuantificationDepthMapper;
import com.huobi.contract.index.dao.QuantificationKlineMapper;
import com.huobi.contract.index.dao.QuantificationTickerMapper;
import com.huobi.contract.index.service.ServiceApplication;
import com.huobi.quantification.huobi.KlineService;
import com.huobi.quantification.index.entity.QuantificationTicker;
@SpringBootTest(classes = ServiceApplication.class)
@RunWith(SpringRunner.class)
public class TestApi {
	@Autowired
	private HuobiApiService huobiApiService;
	@Autowired
	private IndexInfoMapper indexInfoMapper;
	@Autowired
	private QuantificationDepthMapper quantificationDepthMapper;
	@Autowired
	private QuantificationKlineMapper quantificationKlineMapper;
	@Autowired
	private QuantificationTickerMapper quantificationTickerMapper;
	@Autowired
	private KlineService klineService;
		/*@Test
	    public void insert() {
		// huobiApiService.qntificationTickerMapperinsert();
		 QuantificationTicker record=new QuantificationTicker();
		 record.setId(300l);
		 quantificationTickerMapper.insert(record);
	 }*/
	/* @Test
	 public void IindexInfoMapper() {
		 indexInfoMapper.listAvaidlIndexInfo();
	 }*/
	/* @Test
	 public void quantificationDepthMapper() {
		 QuantificationDepth record=new QuantificationDepth();
		 record.setId(300L);
		 quantificationDepthMapper.insert(record);
	 }
	@Test
	 public void quantificationKlineMapper() {
		 QuantificationKline kline=new QuantificationKline();
		 kline.setId(300L);
		 quantificationKlineMapper.insert(kline);
	 }*/
	 @Test
	 public void kline(){
		 klineService.insertKline();
	 }
}
