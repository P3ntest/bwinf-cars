package nl.julius.bwinf.cars;

import javax.naming.PartialResultException;

public class Main {
    public static void main(String[] args) {
        Parser.getFile();

        Wrapper wrapper = new Wrapper(10);

        wrapper.addCar(0, "a");
        wrapper.addCar(3, "b");
        wrapper.addCar(8, "c");
        wrapper.addCar(6, "d");

        Parser.parse().run();

        /*
        0 1 2 3 4 5 6 7 8 9
        a a   b b   d d c c
         */
    }
}
