package com.huobi.contract.index.taskjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dangdang.ddframe.job.api.JobExecutionMultipleShardingContext;
import com.dangdang.ddframe.job.plugin.job.type.simple.AbstractSimpleElasticJob;
import com.huobi.contract.index.contract.index.service.IndexGrabService;

/**
 * { "success": true, "terms": "https:\/\/currencylayer.com\/terms", "privacy":
 * "https:\/\/currencylayer.com\/privacy", "timestamp": 1520318239, "source":
 * "USD", "quotes": { "USDAED": 3.672011, "USDAFN": 68.849998, "USDALL":
 * 106.599998, "USDAMD": 480.290009, "USDANG": 1.782499, "USDAOA": 212.636993,
 * "USDARS": 20.183961, "USDAUD": 1.286602, "USDAWG": 1.78, "USDAZN": 1.699598,
 * "USDBAM": 1.586797, "USDBBD": 2, "USDBDT": 82.949997, "USDBGN": 1.588602,
 * "USDBHD": 0.3768, "USDBIF": 1750.97998, "USDBMD": 1, "USDBND": 1.3153,
 * "USDBOB": 6.849963, "USDBRL": 3.242299, "USDBSD": 1, "USDBTC": 8.9e-5,
 * "USDBTN": 65.074997, "USDBWP": 9.619594, "USDBYN": 1.969745, "USDBYR": 19600,
 * "USDBZD": 1.997799, "USDCAD": 1.29831, "USDCDF": 1565.502706, "USDCHF":
 * 0.939897, "USDCLF": 0.02195, "USDCLP": 598.099976, "USDCNY": 6.342499,
 * "USDCOP": 2853.5, "USDCRC": 566.400024, "USDCUC": 1, "USDCUP": 26.5,
 * "USDCVE": 89.330002, "USDCZK": 20.590099, "USDDJF": 176.830002, "USDDKK":
 * 6.033897, "USDDOP": 49.419998, "USDDZD": 113.892998, "USDEGP": 17.559999,
 * "USDERN": 14.990389, "USDETB": 27.200001, "USDEUR": 0.809899, "USDFJD":
 * 2.010022, "USDFKP": 0.722097, "USDGBP": 0.72281, "USDGEL": 2.4485, "USDGGP":
 * 0.72282, "USDGHS": 4.438499, "USDGIP": 0.722301, "USDGMD": 46.799999,
 * "USDGNF": 9005.000176, "USDGTQ": 7.336043, "USDGYD": 204.690002, "USDHKD":
 * 7.833102, "USDHNL": 23.493999, "USDHRK": 6.016404, "USDHTG": 63.839762,
 * "USDHUF": 254.330002, "USDIDR": 13765, "USDILS": 3.463994, "USDIMP": 0.72282,
 * "USDINR": 64.997498, "USDIQD": 1184, "USDIRR": 37383.000099, "USDISK":
 * 99.999901, "USDJEP": 0.72282, "USDJMD": 126.150002, "USDJOD": 0.708505,
 * "USDJPY": 106.191002, "USDKES": 101.050003, "USDKGS": 68.014999, "USDKHR":
 * 4006.000431, "USDKMF": 398.74998, "USDKPW": 900.00005, "USDKRW": 1074.709961,
 * "USDKWD": 0.299796, "USDKYD": 0.82029, "USDKZT": 319.200012, "USDLAK":
 * 8273.999815, "USDLBP": 1509.999908, "USDLKR": 155.100006, "USDLRD":
 * 130.639999, "USDLSL": 11.824946, "USDLTL": 0.355988, "USDLVL": 0.62055,
 * "USDLYD": 1.328799, "USDMAD": 9.182799, "USDMDL": 16.643017, "USDMGA":
 * 3089.999988, "USDMKD": 49.610001, "USDMMK": 1335.000045, "USDMNT":
 * 2389.999693, "USDMOP": 8.062404, "USDMRO": 350.000197, "USDMUR": 31.809999,
 * "USDMVR": 15.569746, "USDMWK": 717.99976, "USDMXN": 18.778024, "USDMYR":
 * 3.902005, "USDMZN": 61.320092, "USDNAD": 11.818979, "USDNGN": 355.99971,
 * "USDNIO": 31.059099, "USDNOK": 7.81468, "USDNPR": 103.943802, "USDNZD":
 * 1.380899, "USDOMR": 0.384802, "USDPAB": 1, "USDPEN": 3.246499, "USDPGK":
 * 3.239, "USDPHP": 51.990002, "USDPKR": 110.519997, "USDPLN": 3.392898,
 * "USDPYG": 5501.299805, "USDQAR": 3.639801, "USDRON": 3.73296, "USDRSD":
 * 95.517601, "USDRUB": 56.419998, "USDRWF": 842.02002, "USDSAR": 3.74962,
 * "USDSBD": 7.779597, "USDSCR": 13.429787, "USDSDG": 17.9862, "USDSEK":
 * 8.24869, "USDSGD": 1.31828, "USDSHP": 0.722298, "USDSLL": 7630.000351,
 * "USDSOS": 571.999935, "USDSRD": 7.419627, "USDSTD": 19849.300781, "USDSVC":
 * 8.749774, "USDSYP": 514.97998, "USDSZL": 11.828801, "USDTHB": 31.370001,
 * "USDTJS": 8.825299, "USDTMT": 3.4, "USDTND": 2.399195, "USDTOP": 2.231098,
 * "USDTRY": 3.808502, "USDTTD": 6.726024, "USDTWD": 29.275999, "USDTZS":
 * 2247.000254, "USDUAH": 26.430223, "USDUGX": 3641.000129, "USDUSD": 1,
 * "USDUYU": 28.39471, "USDUZS": 8135.00015, "USDVEF": 35189.999805, "USDVND":
 * 22769, "USDVUV": 104.660004, "USDWST": 2.562897, "USDXAF": 530.969971,
 * "USDXAG": 0.06075, "USDXAU": 0.000756, "USDXCD": 2.694795, "USDXDR":
 * 0.689641, "USDXOF": 530.969971, "USDXPF": 96.566957, "USDYER": 249.850006,
 * "USDZAR": 11.829798, "USDZMK": 9001.220523, "USDZMW": 9.710334, "USDZWL":
 * 322.355011 } }
 * 
 * @author oceanzhuang
 *
 */
@Service("exchangeRateGrabJob")
public class ExchangeRateGrabJob extends AbstractSimpleElasticJob {
	private static final Logger LOG = LoggerFactory.getLogger(ExchangeRateGrabJob.class);
	@Autowired
	@Qualifier("exchangeRateGrabService")
	private IndexGrabService indexGrabService;

	@Transactional
	@Override
	public void process(JobExecutionMultipleShardingContext arg0) {
		LOG.info("ExchangeRate抓取任务开始");
		indexGrabService.grab();
		LOG.info("ExchangeRate抓取任务结束");
	}

}
