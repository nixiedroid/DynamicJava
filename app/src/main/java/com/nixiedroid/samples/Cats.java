package com.nixiedroid.samples;

import java.io.*;

public class Cats {
    public static class SimpleCat{
        int a = 5;
        //01 00 00 00 00 00 00 00 00 3c 00 01 05 00 00 00
        //SizeOF = 16
    }
    public static class DoubleCat{
        int a = 5;
        int b = 0xAF00FFFA;
        //01 00 00 00 00 00 00 00 00 3c 00 01 05 00 00 00 fa ff 00 af 00 00 00 00
        //SizeOF = 24
    }
    public static class TripleCat{
        public int a = 5;
        int b = 0xAF00FFFA;
        int c = 0xCA11;
        //01 00 00 00 00 00 00 00 00 3c 00 01 05 00 00 00 fa ff 00 af 11 ca 00 00
        //SizeOF = 24
    }
    public static class MoreCat implements Serializable {
        private int a;
        private int b;

        public int getA() {
            return a;
        }

        public void setA(int a) {
            this.a = a;
        }

        public int getB() {
            return b;
        }

        public void setB(int b) {
            this.b = b;
        }

        public MoreCat(int a, int b) {
            this.a = a;
            this.b = b;
        }
    }

   // public Cat walk(){ return this;}
}
