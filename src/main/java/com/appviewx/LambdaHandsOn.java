package com.appviewx;

import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Predicate;

public class LambdaHandsOn {
    public static void main(String[] args) {

        TwoDimensionalShape newSquare = new Square(2);
        newSquare.area();

        // Using a Lambda Expression and passing it as parameter to the print() method.
        Tutorial onlineTutorial = () -> System.out.println("Online Tutorial");

        // Create an implementation of an interface using an Anonymous inner class
        Tutorial offlineTutorial = new Tutorial() {
            @Override
            public void printTutorialName() {
                System.out.println("Offline Tutorial");
            }
        };

        List<String> testList = new ArrayList<>();
        testList.add("First");
        testList.add("Second");
        Consumer<String> consumer = new Consumer<String>() {
            @Override
            public void accept(String s) {
                System.out.println("First Char for String " + s + " is : " + s.charAt(0));
            }
        };


        Predicate<String> strContainsPredicate = new Predicate<String>() {
            @Override
            public boolean test(String s) {
                return s.contains("rst");
            }
        };

        testList.forEach(consumer);
        testList.stream().filter(strContainsPredicate).forEach((s) -> System.out.println("Filtered String is " + s));

        print(onlineTutorial);
        print(offlineTutorial);

    }

    static void print(Tutorial tutorial) {
        tutorial.printTutorialName();;
    }

    static boolean isStrPresent( String s) {
        return s.contains("rst");
    }
}
