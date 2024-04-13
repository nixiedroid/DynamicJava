package com.nixiedroid.records;

import java.io.Serializable;

public record Book(
        int pages,
        String name
) implements Serializable {
    //Java 14+ feature
    public Book{
        if (pages<0) throw new IllegalArgumentException("Pages must be grater than 0");
    }
    public Book(int pages){
         this(pages,"NONAME");
    }

    //Not allowed: fields are static
//    public void setPages(int pages){
//        this.pages = pages;
//    }


}
