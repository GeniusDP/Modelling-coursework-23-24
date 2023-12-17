package kpi.zaranik;

import java.util.ArrayList;
import java.util.List;
import kpi.zaranik.element.Element;
import kpi.zaranik.element.Floor;
import kpi.zaranik.element.Generator;
import kpi.zaranik.element.Lifting;
import kpi.zaranik.element.observer.IncomeObserver;
import kpi.zaranik.element.observer.LiftingObserver;
import kpi.zaranik.element.observer.Observer;
import kpi.zaranik.element.observer.ProcessedPeopleObserver;
import kpi.zaranik.element.observer.QueuesObserver;
import kpi.zaranik.element.visitor.TaskExecutionVisitor;
import kpi.zaranik.model.Modeller;

public class Main {

    public static void main(String[] args) {

        int totalModellingTime = 1000_000;

        var processedPeopleObserver = new ProcessedPeopleObserver();
        var liftingObserver = new LiftingObserver(totalModellingTime);
        var queuesObserver = new QueuesObserver();
        var incomeObserver = new IncomeObserver();
        List<Observer> observers = new ArrayList<>(
            List.of(incomeObserver, processedPeopleObserver, liftingObserver, queuesObserver)
        );

        List<Element> elements = initElements();
        var visitor = new TaskExecutionVisitor(elements, processedPeopleObserver, liftingObserver, queuesObserver, incomeObserver);

        var modeller = new Modeller(totalModellingTime, elements, visitor);
        modeller.simulate();

        observers.forEach(Observer::printResult);
    }

    private static List<Element> initElements() {
        var lift12 = new Lifting(1, 2);
        var lift21 = new Lifting(2, 1);
        var lift23 = new Lifting(2, 3);
        var lift32 = new Lifting(3, 2);
        var lift34 = new Lifting(3, 4);
        var lift43 = new Lifting(4, 3);
        var lift45 = new Lifting(4, 5);
        var lift54 = new Lifting(5, 4);

        var floor1 = new Floor(1);
        var floor2 = new Floor(2);
        var floor3 = new Floor(3);
        var floor4 = new Floor(4);
        var floor5 = new Floor(5);

        var generator = new Generator(lift12);

        // floor: next and prev lifting
        floor1.setLiftingToNextFloor(lift12);
        floor1.setLiftingToPrevFloor(null);

        floor2.setLiftingToNextFloor(lift23);
        floor2.setLiftingToPrevFloor(lift21);

        floor3.setLiftingToNextFloor(lift34);
        floor3.setLiftingToPrevFloor(lift32);

        floor4.setLiftingToNextFloor(lift45);
        floor4.setLiftingToPrevFloor(lift43);

        floor5.setLiftingToNextFloor(null);
        floor5.setLiftingToPrevFloor(lift54);

        // lift.destinationFloor
        lift12.setDestinationFloor(floor2);
        lift21.setDestinationFloor(floor1);

        lift23.setDestinationFloor(floor3);
        lift32.setDestinationFloor(floor2);

        lift34.setDestinationFloor(floor4);
        lift43.setDestinationFloor(floor3);

        lift45.setDestinationFloor(floor5);
        lift54.setDestinationFloor(floor4);

        //lift.nextLifting
        lift12.setNextLifting(lift23);
        lift21.setNextLifting(null);

        lift23.setNextLifting(lift34);
        lift32.setNextLifting(lift21);

        lift34.setNextLifting(lift45);
        lift43.setNextLifting(lift32);

        lift45.setNextLifting(null);
        lift54.setNextLifting(lift43);

        return new ArrayList<>(List.of(
            generator, floor1, floor2, floor3, floor4, floor5,
            lift12, lift21, lift23, lift32, lift34, lift43, lift45, lift54
        ));
    }

}
