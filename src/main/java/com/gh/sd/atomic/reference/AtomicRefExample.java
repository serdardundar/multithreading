package com.gh.sd.atomic.reference;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicRefExample {

    public static void main(String[] args) {

        String oldName = "old name";
        String newName = "new name";

        AtomicReference<String> atomicReference = new AtomicReference<>(oldName);
        atomicReference.set("Unexpected name");
        if(atomicReference.compareAndSet(oldName, newName)) {
            System.out.println("New value is " + atomicReference.get());
        } else {
            System.out.println("Nothing changed");
        }
    }
}
