package cn.huobi.framework.util;

/**
 * @author liuks
 * 随机产生字符
 *
 */
public class RandomNumber {
    /**
     * 随机产生字符编码
     * @param str 编码以str开头
     * @param length 随机字符的长度(不含开头字符)
     * @param codeState 0 数字;1小写字母;2大写字母;3字母;4字母数字
     * @return
     */
    public static String mumberRandom(String str,int length,int codeState){
        if(length<=0||codeState<0){
            return null;
        }
        if(str==null){
             str="";
        }
        String KeyString=null;
        //随机字符串的随机字符库
        if(codeState==0){
            KeyString = "0123456789";
        }
        if(codeState==1){
             KeyString = "abcdefghijklmnopqrstuvwxyz";
        }
        if(codeState==2){
             KeyString = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if(codeState==3){
            KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        }
        if(codeState==4){
            KeyString = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        }

        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        String reStr=str+sb.toString();
        return reStr;
    }
}
