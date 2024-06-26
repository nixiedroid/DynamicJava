package com.nixiedroid.function;

import java.util.function.BiFunction;
import java.util.function.IntPredicate;
import java.util.function.UnaryOperator;


public class Functions {
    public Functions(){
        System.out.println(doProcess(new UnaryOperator<Integer>() {
            @Override
            public Integer apply(Integer integer) {
                return integer++;
            }
        }));
        System.out.println(doProcess(integer -> integer*2));
        System.out.println(doProcess(integer ->{
            integer +=5;
            return integer/2;
        }));
        System.out.println(isFits(value -> {
            if (value - 1 < 0) return value ==0;
            return value >= 0;
        },-1));
        System.out.println(isFits(value -> {
            return value >= 0;
        },-1));

        System.out.println(isFits(value -> value >= 0,-1));
        System.out.println(isFitsInv(5,value -> value>=0));

        System.out.println(new CheckTwo() {
            @Override
            public boolean doCheck(int val) {
                doChee(5);
                return val<0;
            }

            @Override
            public boolean doChee(int val2) {
                return false;
            }
        }.doCheck(7));
        System.out.println(new Check() {
            @Override
            public boolean doCheck(int val) {
                return val<0;
            }
        }.doCheck(-1));
        Check a = (Check) val -> val<0;
        System.out.println(a.doCheck(5));
        new Thread(()-> System.out.println("4")).start();
    }
    boolean recvTwoVals(BiFunction<Integer,Integer,Boolean> func,int a,int b){
        return func.apply(a,b);
    }
    Integer doProcess(UnaryOperator<Integer> fun){
        return fun.apply(5);
    }
    Boolean isFitsInv(int val,IntPredicate fun){
        return fun.test(val);
    }
    Boolean isFits(IntPredicate fun,int val){
        return fun.test(val);
    }
    interface CheckTwo{
        boolean doCheck(int val);
        boolean doChee(int val2);
    }
    interface Check{
        boolean doCheck(int val);
    }
    class Printer{
        void printOne(){

        }
    }
}
