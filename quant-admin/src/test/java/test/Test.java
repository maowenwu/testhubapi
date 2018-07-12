package test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

public class Test {

    static class A{
        int num = 6;
        int price = 0;
        List<Integer> list = new ArrayList<>();

        public A(int num, int price) {
            this.num = num;
            this.price = price;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public double getPrice() {
            return price;
        }

        public void setPrice(int price) {
            this.price = price;
        }

        public  List<Integer> getList() {
            List<Integer> list = new ArrayList<>();
            if(this.num > 0){
                for(int i =0; i < this.num; i++){
                    list.add(i);
                }
            }
            return list;
        }

        public int getArrOne(int i){
            return getList().get(i%(this.num + 1)) * this.price;
        }
    }

    public static void main(String[] args) {

        A a = new A(2, 1);
        A b = new A(3,  5);
        A c = new A(1,  10);
        List<A> list = new ArrayList<>();
        list.add(a);
        list.add(b);
        list.add(c);
        int maxType = 1;//总共可能出现的情况总数
        for(A item: list){
            maxType = maxType * (item.num + 1);
        }
        TreeSet set = new TreeSet<>();

        for(int i = 1; i <= maxType ; i++){
            int sum = 0;
            //计算每种情况下的sum
            for(A item: list){
                sum += item.getArrOne(i);
            }
            set.add(sum);
        }
        System.out.println(Arrays.toString(set.toArray()));
        System.out.println(set.size());
    }

}
