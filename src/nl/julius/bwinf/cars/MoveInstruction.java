package nl.julius.bwinf.cars;

import java.util.List;

public class MoveInstruction {
    Car car;
    int delta;

    public MoveInstruction(Car car, int delta) {
        this.car = car;
        this.delta = delta;
    }

    public static void printInstructionList(List<MoveInstruction> list) {
        for (MoveInstruction instruction : list) {
            System.out.print(instruction.car.key.toUpperCase() + " " + instruction.delta + ", ");
        }
    }
}
