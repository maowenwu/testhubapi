package cn.huobi.framework.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import cn.huobi.framework.model.Result;
import cn.huobi.framework.model.UserLoginInfo;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonUtil {

    private static final Pattern patternYMD = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2}$");
    private static final Pattern patternYMDHMS = Pattern.compile("^(\\d){4}-(\\d){1,2}-(\\d){1,2} (\\d){1,2}:(\\d){1,2}:(\\d){1,2}$");


    /**
     * 获取当前登录的UserLoginInfo
     * @return
     */
    public static UserLoginInfo getLoginUser(){
        UserLoginInfo userInfo = null;
        final UserLoginInfo principal = (UserLoginInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal != null){
            userInfo = principal;
        } else {
            throw new BossBaseException("用户未登录");
        }
        return userInfo;
    }

    /**
     * 将图片的地址转换为阿里云的地址，boss-temp
     * @param imgUrl
     * @return
     */
    public static String getImgUrl(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        if(imgUrl.startsWith("http")){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_TEMP_TUCKET, imgUrl, date);
        return imgUrl;
    }

    /**
     * 将图片的地址转换为阿里云的地址，agent-attch
     * @param imgUrl
     * @return
     */
    public static String getImgUrlAgent(String imgUrl){
        if(StringUtils.isBlank(imgUrl)){
            return imgUrl;
        }
        if(imgUrl.startsWith("http")){
            return imgUrl;
        }
        Long dateTime = new Date().getTime() + 60*60*1000L;
        Date date = new Date(dateTime);
        imgUrl = ALiYunOssUtil.genUrl(Constants.ALIYUN_OSS_ATTCH_TUCKET, imgUrl, date);
        return imgUrl;
    }

    /**
     * 校验excel file的大小和格式
     * @param file
     * @param result
     * @return
     */
    public static boolean checkExcelFile(MultipartFile file, Result result) {
        if(file == null){
            result.setMsg("请选择上传文件");
            return false;
        }
        if (!file.isEmpty()) {
            if(((file.getSize()/1024)/1024) > 4){
                result.setMsg("上传文件过大,请上传4MB以内的文件");
                return false;
            }
            String format=file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
            if(!format.equals(".xls") && !format.equals(".xlsx")){
                result.setMsg("请选择.xls或.xlsx文件");
                return false;
            }
        }
        return true;
    }

    public static Map<String, Object> getMonthMap(){
        Map<String, Object> map = new HashMap<>();
        map.put("一月", "01");
        map.put("二月", "02");
        map.put("三月", "03");
        map.put("四月", "04");
        map.put("五月", "05");
        map.put("六月", "06");
        map.put("七月", "07");
        map.put("八月", "08");
        map.put("九月", "09");
        map.put("十月", "10");
        map.put("十一月", "11");
        map.put("十二月", "12");
        map.put("1", "01");
        map.put("2", "02");
        map.put("3", "03");
        map.put("4", "04");
        map.put("5", "05");
        map.put("6", "06");
        map.put("7", "07");
        map.put("8", "08");
        map.put("9", "09");
        map.put("01", "01");
        map.put("02", "02");
        map.put("03", "03");
        map.put("04", "04");
        map.put("05", "05");
        map.put("06", "06");
        map.put("07", "07");
        map.put("08", "08");
        map.put("09", "09");
        map.put("10", "10");
        map.put("11", "11");
        map.put("12", "12");
        map.put("Jan", "01");
        map.put("Feb", "02");
        map.put("Mar", "03");
        map.put("Apr", "04");
        map.put("May", "05");
        map.put("Jun", "06");
        map.put("Jul", "07");
        map.put("Aug", "08");
        map.put("Sep", "09");
        map.put("Oct", "10");
        map.put("Nov", "11");
        map.put("Dec", "12");
        return map;
    }

    public static Date getFormatDate(String dateStr) {
        Date date;
        Matcher matcherYMDHMS = patternYMDHMS.matcher(dateStr);
        if(matcherYMDHMS.matches()){
            date = DateUtil.parseLongDateTime(dateStr);
            return date;
        }
        Matcher matcherYMD = patternYMD.matcher(dateStr);
        if(matcherYMD.matches()){
            date = DateUtil.parseDateTime(dateStr);
            return date;
        }
        ;Map<String, Object> monthMap = getMonthMap();
        String[] issueDateArr = dateStr.split("-");
        dateStr = issueDateArr[2] + "-" + monthMap.get(issueDateArr[1]) + "-" + issueDateArr[0];
        date = DateUtil.parseDateTime(dateStr);
        return date;
    }
}
