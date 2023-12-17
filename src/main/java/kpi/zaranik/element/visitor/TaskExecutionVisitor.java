package kpi.zaranik.element.visitor;

import static kpi.zaranik.util.Constants.INF_TIME;
import static kpi.zaranik.util.Constants.LIFTING_TRAVEL_TIME;
import static kpi.zaranik.util.Constants.LIFT_CAPACITY;
import static kpi.zaranik.util.Constants.NUMBER_OF_FLOORS;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;
import kpi.zaranik.dto.Person;
import kpi.zaranik.element.Element;
import kpi.zaranik.element.Floor;
import kpi.zaranik.element.Generator;
import kpi.zaranik.element.LiftState;
import kpi.zaranik.element.Lifting;
import kpi.zaranik.element.observer.IncomeObserver;
import kpi.zaranik.element.observer.LiftingObserver;
import kpi.zaranik.element.observer.ProcessedPeopleObserver;
import kpi.zaranik.element.observer.QueuesObserver;
import kpi.zaranik.model.TimeHolder;
import kpi.zaranik.util.FunRand;

public class TaskExecutionVisitor implements Visitor {

    private final ProcessedPeopleObserver processedPeopleObserver;
    private final LiftingObserver liftingObserver;
    private final QueuesObserver queuesObserver;
    private final IncomeObserver incomeObserver;
    private final List<Lifting> allLiftings;

    public TaskExecutionVisitor(
        List<Element> elements,
        ProcessedPeopleObserver processedPeopleObserver,
        LiftingObserver liftingObserver,
        QueuesObserver queuesObserver,
        IncomeObserver incomeObserver
    ) {
        this.processedPeopleObserver = processedPeopleObserver;
        this.liftingObserver = liftingObserver;
        this.queuesObserver = queuesObserver;
        this.incomeObserver = incomeObserver;
        this.allLiftings = elements.stream().filter(e -> e instanceof Lifting).map(Lifting.class::cast).toList();
    }

    @Override
    public void visitGenerator(Generator generator) {
        incomeObserver.increase(1);
        var newPerson = Person.builder()
            .finishWorkOnFloorTime(INF_TIME)
            .destinationFloorNumber(getNextFloorForPerson(1))
            .build();
        Lifting firstLifting = generator.getFirstLifting();
        firstLifting.appendQueue(newPerson);
        if (LiftState.WAITING) {
            LiftState.WAITING = false;
            firstLifting.setTimeNext(TimeHolder.now() + 1);
        }
    }

    @Override
    public void visitFloor(Floor floor) {
        int currFloorNumber = floor.getNumber();

        Set<Person> peopleOnFloor = floor.getPeople();

        List<Person> finishedWork = peopleOnFloor.stream()
            .filter(p -> p.getFinishWorkOnFloorTime() == TimeHolder.now())
            .toList();
        finishedWork.forEach(peopleOnFloor::remove);

        if (!finishedWork.isEmpty() && LiftState.WAITING) {
            LiftState.WAITING = false;
            Lifting firstLifting = allLiftings.stream().filter(l -> l.getStartFloorNumber() == 1).findFirst().orElseThrow();
            firstLifting.setTimeNext(TimeHolder.now() + 1);
        }

        // move all to lifting stage/lifting queue
        finishedWork.forEach(person -> {
            person.setFinishWorkOnFloorTime(INF_TIME);
            person.setDestinationFloorNumber(getNextFloorForPerson(currFloorNumber));
            person.setTimeStandToQueue(TimeHolder.now());
            boolean wantsUpper = person.getDestinationFloorNumber() - currFloorNumber > 0;
            Lifting lifting;
            if (wantsUpper) {
                lifting = floor.getLiftingToNextFloor();
            } else {
                lifting = floor.getLiftingToPrevFloor();
            }
            lifting.appendQueue(person);
        });
    }

    @Override
    public void visitLifting(Lifting lifting) {
        liftingObserver.observe(lifting);
        lifting.setTimeNext(INF_TIME);
        Floor currentFloor = lifting.getDestinationFloor();

        List<Person> peopleOnBoard = lifting.getPeopleOnBoard();

        List<Person> goOutHere = peopleOnBoard.stream()
            .filter(p -> p.getDestinationFloorNumber() == currentFloor.getNumber())
            .toList();
        peopleOnBoard.removeAll(goOutHere);
        if (currentFloor.getNumber() != 1) {
            goOutHere.forEach(p -> p.setFinishWorkOnFloorTime(getFinishWorkOnFloorTime()));
            currentFloor.getPeople().addAll(goOutHere);
        } else {
            processedPeopleObserver.increase(goOutHere.size());
        }

        Lifting nextLifting = getNextLifting(lifting);
        if (nextLifting != null) {
            int freePlaces = LIFT_CAPACITY - peopleOnBoard.size();
            List<Person> fromQueue = nextLifting.getQueue().stream().limit(freePlaces).toList();
            nextLifting.getQueue().removeAll(fromQueue);

            peopleOnBoard.addAll(fromQueue);

            nextLifting.setPeopleOnBoard(peopleOnBoard);
            nextLifting.setTimeNext(TimeHolder.now() + LIFTING_TRAVEL_TIME);
        }
        lifting.setPeopleOnBoard(new ArrayList<>());
    }

    private Lifting getNextLifting(Lifting liftingJustFinished) {
        List<Person> peopleOnBoard = liftingJustFinished.getPeopleOnBoard();
        if (!peopleOnBoard.isEmpty()) {
            return liftingJustFinished.getNextLifting();
        }

        Floor currentFloor = liftingJustFinished.getDestinationFloor();
        // on current floor QUEUE UP exists
        Lifting liftingToNextFloor = currentFloor.getLiftingToNextFloor();
        if (liftingToNextFloor != null && !liftingToNextFloor.getQueue().isEmpty()) {
            return liftingToNextFloor;
        }
        // on current floor QUEUE DOWN exists
        Lifting liftingToPrevFloor = currentFloor.getLiftingToPrevFloor();
        if (liftingToPrevFloor != null && !liftingToPrevFloor.getQueue().isEmpty()) {
            return liftingToPrevFloor;
        }

        // people on FLOOR UPPER are WAITING
        for (Lifting lifting : allLiftings) {
            if (lifting != null && lifting.getStartFloorNumber() > currentFloor.getNumber() && !lifting.getQueue().isEmpty()) {
                return liftingToNextFloor;
            }
        }
        // people on FLOOR LOWER are WAITING
        for (Lifting lifting : allLiftings) {
            if (lifting != null && lifting.getStartFloorNumber() < currentFloor.getNumber() && !lifting.getQueue().isEmpty()) {
                return liftingToPrevFloor;
            }
        }

        if (currentFloor.getNumber() == 1) {
            LiftState.WAITING = true;
            return null;
        }
        return liftingToPrevFloor;
    }

    private int getFinishWorkOnFloorTime() {
        return TimeHolder.getTime() + FunRand.uniform(15, 120) * 60;
    }

    private int getNextFloorForPerson(int currentFloorNumber) {
        if (currentFloorNumber < 1) {
            throw new IllegalArgumentException("currentFloorNumber cannot be < 1");
        }
        if (currentFloorNumber == 1) {
            return FunRand.uniform(2, 5);
        }
        double random = Math.random();
        if (random <= 0.7) {
            return 1;
        }
        List<Integer> restFloors = IntStream.rangeClosed(1, NUMBER_OF_FLOORS)
            .filter(number -> number != 1 && number != currentFloorNumber)
            .boxed()
            .toList();
        return restFloors.get(FunRand.uniform(0, 2));
    }

}
