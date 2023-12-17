package kpi.zaranik.element.observer;

public class IncomeObserver implements Observer {

    private int income;

    public void increase(int value) {
        this.income += value;
    }

    @Override
    public void printResult() {
        System.out.println("Number of income people = " + income);
    }
}
