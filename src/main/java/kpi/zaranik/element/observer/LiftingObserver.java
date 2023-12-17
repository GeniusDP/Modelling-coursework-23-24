package kpi.zaranik.element.observer;

import static kpi.zaranik.util.Constants.LIFTING_TRAVEL_TIME;

import kpi.zaranik.element.Lifting;

public class LiftingObserver implements Observer {

    private final int totalModellingTime;
    private int timeLiftIsCarryingPassengers;
    private int timeLiftIsEmpty;
    private int maxPeopleOnBoard;
    private int observedTimes;
    private int sumPeopleOnBoard;

    public LiftingObserver(int totalModellingTime) {
        this.totalModellingTime = totalModellingTime;
    }

    public void observe(Lifting liftingJustFinished) {
        this.observedTimes ++;
        if (!liftingJustFinished.getPeopleOnBoard().isEmpty()) {
            this.timeLiftIsCarryingPassengers += LIFTING_TRAVEL_TIME;
        } else {
            this.timeLiftIsEmpty += LIFTING_TRAVEL_TIME;
        }

        int peopleOnBoard = liftingJustFinished.getPeopleOnBoard().size();
        this.sumPeopleOnBoard += peopleOnBoard;
        this.maxPeopleOnBoard = Math.max(peopleOnBoard, this.maxPeopleOnBoard);
    }

    @Override
    public void printResult() {
        int notMove = totalModellingTime - (timeLiftIsCarryingPassengers + timeLiftIsEmpty);
        System.out.println("Time lift is carrying passengers: " + timeLiftIsCarryingPassengers + " sec (" + (timeLiftIsCarryingPassengers * 100.) / totalModellingTime + "%)");
        System.out.println("Time lift is empty: " + timeLiftIsEmpty + " sec (" + (timeLiftIsEmpty * 100.) / totalModellingTime + "%)");
        System.out.println("Time lift does not move: " + notMove + " sec (" + (notMove * 100.) / totalModellingTime + "%)");
        System.out.println("Max people on board: " + maxPeopleOnBoard);
        System.out.println("Avg people on board: " + (sumPeopleOnBoard + 0.) / observedTimes);
    }

}
