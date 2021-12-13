[comment]: <> (WARNING - This file is not intended to read as is. Please view this in an appropriate Markdown viewer.)
## Documentation BWINF Challenge 1 Schiebeparkplatz
---

## The Challenge
The chellenge is to determine which cars need to move and how far, so that each spot is free. The difficulty here is, that cars can be blocking each other.

### My solution structure
I solved this problem using Java 16.0.0 running on Amazon Corretto 16. I used OOP. My preferred editor of choice for this was IntelliJ IDEA Community Edition 2019.x.x. The current solution works for all test samples plus addition 10 I created to test it further. The program omits all of its solutions to `std::out` at runtime.

### My attempt
My initial Attempt was to loop through the cars twice for every spot, however I quickly realized that some setups require multiple sequential movements. So I realized I need a recursion to solve this.

## Step by Step code breakdown

1. First of all I have to read in the challenge from a *txt* file. For this I used a helper function `Parser::getFile():String` which reads the file and returns it as a UTF-8 String. I can then use this in the parse function to read in the challenge into my OOP:
    ```java
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
    ```
    I start of by reading the file and splitting it into an array of all lines (separated by `"\n"`). I then figure out the *key* of the last car in the vertical parking spots, since I already now the first one is `"A"`. Using this and a reference to the alphabet, I can determine how many cars there are. Now I initialize the Wrapper class with the just defined amount of cars. The next step is to add all the horizontally oriented cars. The code iterates through the remaining lines, since that is where the cars are located - according to the official documentation. Since every line of horizontal cars already has all the info I need, I just parse it and pass it into the `Wrapper:addCar():void` method as parameters. In the end I simply return the just parsed and interpreted challenge.

2. I call the `Wrapper:run():void` method to start the actual algorythim and print it to `std::in`.
    ```java
    public void run() {
        for (int i = 0; i < spacesAmount; i++) {
            checkSpace(i);
        }
    }
    ``` 
    This function iterates all vertical parking spaces and runs the the method checkSpace, which will solve the challenge for each individual car.

2. Checking an individual Space
    
    We start of by printing the current cars *key* and a `": "` to std::out.
    ```java
    String positionKey = "" + ((char) (65 + position));

    System.out.print(positionKey + ": ");
    ```
    After that we check if there even is a car blocking.
    ```java
    Car blockingCar = null;
    for (Car car : cars) {
        if (blockingCar != null) continue;
        if (car.isBlocking(position)) {
            blockingCar = car;
        }
    }
    ``` 
    If there is none, we end the line and `continue` to the nextone.
    ```java
    if (blockingCar == null) {
        System.out.println();
        return;
    }
    ```
    If there is a car blocking however, we check the effort for moving it in both directions.
    ```java
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
    ```
    We execute these lines in a `try {} catch() {}` since I have implemented a custom exception which can be thrown by the `Car::effort():List<MoveInstruction>` function - the `CarBumpException`. This will be thrown whenever a certain direction (either left or right) is impossible to complete. This happens when a car *"bumps"* into a wall while trying to make space. If this happens, the corresponding list will be kept at `null` respectively. After this step, we just have to compare the two.
    ```java
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
    ``` 
    Here we check to see if both are viable options first. If they are, we compare the two and print the less intensive one. However if only one of the two is possible, we just print that one.

    At the very end we just have to omit an empty `"\n"` to `std::out` to complete the current line.

## Other classes and functions used but not yet explained
### `Car::effort():List<MoveInstruction[recursive]`
This is arguably the most important function of this algorithm, since It is and contains the recursion.
```java
public List<MoveInstruction> effort(int delta) throws CarBumpException {
    int target = position + delta;

    if (target < 0 || target > wrapper.spacesAmount - 2) {
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
        List<MoveInstruction> list = new ArrayList<>();
        list.add(effort);
        return list;
    }

    int targetAbsDelta = 1;
    if(blockingCar.position == position) {
        targetAbsDelta = 2;
    }

    int targetDelta = targetAbsDelta * ((delta > 0) ? 1 : -1);
    
    List<MoveInstruction> total = blockingCar.effort(targetDelta);

    total.add(effort);

    return total;
}
```
We start of by calculating the target of the current car based off of the current position and the required `delta`. If this target is out of bounds for the parkin lot the function will throw a *head-declared* exception `CarBumpException` and concludingly terminate. The next step is to check if there is another car blocking our request or if we can just proceed.
```java
Car blockingCar = null;
for (Car car : wrapper.cars) {
    if (blockingCar != null) continue;
    if (car == this) continue;
    if (car.isBlocking(target) || car.isBlocking(target + 1)) {
        blockingCar = car;
    }
}
```
After this, the function creates its own effort
```java
MoveInstruction effort = new MoveInstruction(this, delta);
```
and if there is no blocking car we can just safely return this and thus terminate the recursion.
```java
if (blockingCar == null) {
    List<MoveInstruction> list = new ArrayList<>();
    list.add(effort);
    return list;
}
```
Now we are certain that another car is blocking us. The next step is to check how far said car has to *absolutely* move in order for us to make enough space.
```java
int targetAbsDelta = 1;
if(blockingCar.position == position) {
    targetAbsDelta = 2;
}
```
We then get the directioned delta for the blocking car by applying the correct number sign.

The last step is to add the own effort onto the chain and continue the recursion until terminated.
```java
List<MoveInstruction> total = blockingCar.effort(targetDelta);

total.add(effort);

return total;
```

### `Car::isBlocking():boolean`
```java
public boolean isBlocking(int position) {
    if (this.position == position || this.position + 1 == position) {
        return true;
    }

    return false;
}
```
This method simply checks if either one of the two spaces once horizontal car occupies is blocking the position `int position`.

### `MoveInstruction`
```java
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
```
This is a wrapper for a single recursive required action executed by a car. It contains the referenced car (`Car car;`) and the action it has to take delta (`int delta;`).

It has one *static* function in order to print a `List` of itself to `std::out`. This function is self explaining and not mission-critical so I will not be explaining it in the scope of this documentaion.

## Final Notes
I chose this challenge because I figured it would be the easiest to complete. During my multiple attempts, I also helped out multiple fellow students and realised a lot of interesting things about this challenge that way. I would say I solved this as efficiently as possible and without any errors. However there is still room for improvement, i.e. you could write this in a more efficient programming language like *C* or implement special cases if one parking spot is impossible to solve.

***AlL Rights Reserved [©](https://www.gesetze-im-internet.de/englisch_urhg/englisch_urhg.html#:~:text=Copyright%20protects%20the%20author%20in,the%20use%20of%20the%20work.&text=The%20author%20has%20the%20right%20to%20be%20identified%20as%20the,designation%20is%20to%20be%20used.) 2021 - [**Julius van Voorden**](https://www.github.com/P3ntest)***
