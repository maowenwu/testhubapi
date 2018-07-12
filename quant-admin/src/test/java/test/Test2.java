package test;

import com.alibaba.fastjson.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Test2 {

    public static void main(String[] args){

//        List<String> list = new ArrayList<>();
//        List<String> list1 = new ArrayList<>();
//        List<String> list2 =  new ArrayList<>();
//        if(list2 != null && list2.size() > 0){
//            list.addAll(list2);
//        }
//        list1.add("1");
//
//        list.addAll(list1);
//
//        System.out.println(JSONObject.toJSONString(list));

        Date nowDate = new Date();
        Long nowTime = nowDate.getTime() - 24*60*60*1000L;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date yesDate = new Date(nowTime);
        System.out.println(yesDate);
        System.out.println(sdf.format(yesDate));


    }


}
