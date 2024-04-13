package com.nixiedroid.records;

import java.io.Serializable;

public record BookCopy(
        int pages,
        String name
) implements Serializable { }