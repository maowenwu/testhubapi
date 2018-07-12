package test;

import org.apache.commons.lang3.StringUtils;

import java.util.*;

public class PageTest {

    public static void main(String[] args){
        String str = " src/main/java/cn/eeepay/framework/daoSuperbank/BankImportDao.java\n" +
                " src/main/java/cn/eeepay/framework/daoSuperbank/OrderMainDao.java\n" +
                " src/main/java/cn/eeepay/framework/daoSuperbank/UserProfitDao.java\n" +
                " src/main/java/cn/eeepay/framework/model/OrderMain.java\n" +
                " src/main/java/cn/eeepay/framework/model/UserProfit.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/SuperBankServiceImpl.java\n" +
                " src/main/java/cn/eeepay/framework/service/impl/TranslateRowImpl.java\n" +
                " src/main/webapp/js/controllers/superBank/bankImportCtrl.js\n" +
                " src/main/webapp/js/controllers/superBank/loanOrderCtrl.js\n" +
                " src/main/webapp/js/controllers/superBank/profitDetailOrderCtrl.js\n" +
                " src/main/webapp/views/superBank/bankImport.html\n" +
                " src/main/webapp/views/superBank/loanOrderDetail.html\n" +
                " src/main/webapp/views/superBank/profitDetailOrder.html";
        convert(str);
    }

    public static String convert(String context){
        if(StringUtils.isBlank(context)){
            return context;
        }
        context = context.replaceAll("src/main/java", "<include name=\"WEB-INF/classes")
        .replaceAll("\\.java", "*.class\"/>")
        .replaceAll("src/main/webapp/", "<include name=\"")
        .replaceAll("\\.html", ".html\"/>")
        .replaceAll("\\.xlsx", ".xlsx\"/>")
        .replaceAll("\\.js", ".js\"/>")
        .replaceAll("\"/>\"/>", "\"/>");

        String[] strArr = context.split("\n");

        if(strArr.length > 0){
            Set<String> setList = new HashSet<>();
            for (String str: strArr){
                str = str.replaceAll("\t", "");
                str = str.trim();
                boolean addStatus = setList.add(str);
                if(addStatus){
                    System.out.println(str);
                }
            }
        }

        return context;
    }
}
