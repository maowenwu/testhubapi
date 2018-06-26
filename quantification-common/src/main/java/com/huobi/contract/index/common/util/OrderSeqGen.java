package com.huobi.contract.index.common.util;

import java.text.SimpleDateFormat;
import java.util.Random;

public class OrderSeqGen {
	private OrderSeqGen() {

	}

	private static int se = 0;

	public static synchronized String createApplyId() {
		String nextFrozeId = "";
		if (se > 99)
			se = 0;
		nextFrozeId = otherdateofnow().substring(2, 8)
				+ getTradeSn().substring(3, 13) + createSerial("" + se, 2);
		se++;
		if (nextFrozeId.length() != 18)
			throw new RuntimeException("申请号长度错误[" + nextFrozeId + "]");
		return nextFrozeId;
	}

	/**
	 * 返回当前日期
	 */
	public static String otherdateofnow() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return sdf.format(new java.util.Date());
	}

	/**
	 * 得到系统的时间戳
	 */
	public static String getTradeSn() {
		return "" + new java.util.Date().getTime();
	}

	/**
	 * 生成固定长度的序列号，不足位数在前补0
	 */
	public static String createSerial(String src, int len) {
		String dest = "";
		if (src.length() >= len) {
			dest = src.substring(0, len);
		} else {
			dest = createSameChar("0", len - src.length()) + src;
		}
		return dest;
	}

	/**
	 * 返回一样字符的串
	 */
	public static String createSameChar(String src, int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(src);
		}
		return sb.toString();
	}

	public static void main(String[] args) {
		System.out.println(createApplyId());
	}	
	/**
	 * 
	 */
	public  static String getRandomString(int length,String ramdString){  
        
        Random random = new Random();  
        StringBuffer sb = new StringBuffer();  
        int len=ramdString.length();  
        for(int i = 0 ; i < length; ++i){  
            int number = random.nextInt(len);//[0,62)  
              
            sb.append(ramdString.charAt(number));  
        }  
        return sb.toString();  
    }  
    /**
     * 获取随机数
     * @param len
     * @return
     */
    public static String getRandomNum(int len){
    	String str="0123456789";
    	return getRandomString(len, str);
    }
    /**
     * 回去含有字母和数字的随机码
     * @param len
     * @return
     */
    public static String getRandomNumAndChar(int len){
    	String str="abcdefghijklmnopqrstuvwxyz0123456789";
    	return getRandomString(len, str);
    }
}
