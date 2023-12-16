package kpi.zaranik.element.observer;

public class ProcessedPeopleObserver implements Observer {

    private int processed;

    public void increase(int value) {
        processed += value;
    }

    @Override
    public void printResult() {
        System.out.println("Total number of processed people = " + processed);
    }
}
