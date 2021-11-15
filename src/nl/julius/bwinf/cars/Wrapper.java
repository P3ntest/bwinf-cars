package nl.julius.bwinf.cars;

import java.util.ArrayList;
import java.util.List;

public class Wrapper {
    int spacesAmount;
    List<Car> cars = new ArrayList<>();

    public Wrapper(int spacesAmount) {
        this.spacesAmount = spacesAmount;
    }

    public void addCar(int position, String key) {
        cars.add(new Car(position, key, this));
    }

    public void run() {
        for (int i = 0; i < spacesAmount; i++) {
            checkSpace(i);
        }
    }

    private void checkSpace(int position) {
        Car blockingCar = null;
        for (Car car : cars) {
            if (blockingCar != null) continue;
            if (car.isBlocking(position)) {
                blockingCar = car;
            }
        }

        String positionKey = "" + ((char) (65 + position));

        System.out.print(positionKey + ": ");

        if (blockingCar == null) {
            System.out.println();
            return;
        }

        List<MoveInstruction> effortLeft = null;
        List<MoveInstruction> effortRight = null;

        try {
            effortLeft = blockingCar.effort(blockingCar.position == position ? -2 : -1);
            ;
        } catch (CarBumpException ignored) {
        }

        try {
            effortRight = blockingCar.effort(blockingCar.position == position ? 1 : 2);
        } catch (CarBumpException ignored) {
        }


        if (effortRight != null && effortLeft != null) {
            if (effortRight.size() > effortLeft.size()) {
                MoveInstruction.printInstructionList(effortLeft);
            } else {
                MoveInstruction.printInstructionList(effortRight);
            }
        } else if (effortRight == null) {
                MoveInstruction.printInstructionList(effortLeft);
        } else {
                MoveInstruction.printInstructionList(effortRight);
        }

        System.out.println();
    }
}
