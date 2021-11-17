package nl.julius.bwinf.cars;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class Parser {
    static String getFile() {
        String data = "";
        try {
            File myObj = new File("parkplatz.txt");
            Scanner myReader = new Scanner(myObj);
            while (myReader.hasNextLine()) {
                data += myReader.nextLine() + "\n";
            }
            myReader.close();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
        return data;
    }

    static Wrapper parse() {
        String[] lines = getFile().split("\n");

        String chars = "abcdefghijklmnopqrstuvwxyz";

        String endCarKey = lines[0].split(" ")[1].toLowerCase();

        int amountCars = chars.indexOf(endCarKey) + 1;

        Wrapper wrapper = new Wrapper(amountCars);

        for (int i = 0; i < Integer.parseInt(lines[1]); i++) {
            String[] elements = lines[i + 2].split(" ");
            wrapper.addCar(Integer.parseInt(elements[1]), elements[0]);
        }

        return wrapper;
    }
}
