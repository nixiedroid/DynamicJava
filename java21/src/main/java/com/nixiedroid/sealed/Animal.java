package com.nixiedroid.sealed;

public sealed abstract class Animal permits Animal.Dog, Cat {
    protected String name;
    final class Dog extends Animal{

    }
}
