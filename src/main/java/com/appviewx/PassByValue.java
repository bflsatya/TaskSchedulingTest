package com.appviewx;

import com.appviewx.model.Student;

public class PassByValue {
    public static void main(String[] args) {

        Student myStudent = new Student();
        myStudent.setAge(10);
        System.out.println("Hashcode inside main {} : " + myStudent);
        incrementAge(myStudent);
        System.out.println(myStudent.getAge());

        int test = 20;
        System.out.println("Primitive value before increment method " + test);
        increment(test);
        System.out.println("Primitive value after increment method " + test);

        Integer testBoxed = 20;
        System.out.println("Boxed value before increment method :::: " + testBoxed.hashCode());
        increment(testBoxed);
        System.out.println("Boxed value after increment method " + testBoxed);

        Integer testBoxedObj = new Integer(20);
        System.out.println("Integer Hashcode in main method {} : " + testBoxedObj);
        System.out.println("Boxed value before increment method " + testBoxedObj);
        increment(testBoxedObj);
        System.out.println("Boxed value after increment method " + testBoxedObj);


    }

    static void incrementAge(Student student) {
        student.setAge(student.getAge() + 1);
        System.out.println("Hashcode inside method {} : " + student);
        student = new Student();
        System.out.println("Hashcode after reassignment inside method {} : " + student);
        System.out.println("Age after reassignment : " + student.getAge());
    }

    static void increment(Integer val) {
        System.out.println("Integer Hashcode inside method {} : " + val);
        val = val+1;
    }
}
