package nl.julius.bwinf.cars;

import javax.security.auth.login.CredentialException;
import java.util.ArrayList;
import java.util.List;

public class Car {
    int position;
    String key;
    Wrapper wrapper;

    public Car(int position, String key, Wrapper wrapper) {
        this.position = position;
        this.key = key;
        this.wrapper = wrapper;
    }

    public boolean isBlocking(int position) {
        if (this.position == position || this.position + 1 == position) {
            return true;
        }

        return false;
    }

    public List<MoveInstruction> effort(int delta) throws CarBumpException {
        //System.out.println(key + " effort " + delta);
        int target = position + delta;

        if (target < 0 || target > wrapper.spacesAmount - 2) {
            //System.out.println(("doesnt work"));
            throw new CarBumpException();
        }

        Car blockingCar = null;
        for (Car car : wrapper.cars) {
            if (blockingCar != null) continue;
            if (car == this) continue;
            if (car.isBlocking(target) || car.isBlocking(target + 1)) {
                blockingCar = car;
            }
        }

        MoveInstruction effort = new MoveInstruction(this, delta);

        if (blockingCar == null) {
            //System.out.println(key + " can move " + delta);
            List<MoveInstruction> list = new ArrayList<>();
            list.add(effort);
            return list;
        }

        int targetAbsDelta = 1;
        if(blockingCar.position == position) {
            targetAbsDelta = 2;
        }

        int targetDelta = targetAbsDelta * ((delta > 0) ? 1 : -1);

            //System.out.println("testing " + blockingCar.key + " to " + targetDelta);

        List<MoveInstruction> total = blockingCar.effort(targetDelta);

        total.add(effort);

        return total;
    }
}