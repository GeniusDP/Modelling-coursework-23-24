package kpi.zaranik.element.observer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kpi.zaranik.dto.Person;
import kpi.zaranik.element.Lifting;
import kpi.zaranik.model.TimeHolder;

public class QueuesObserver implements Observer {

    private final Map<Lifting, QueueState> map;

    public QueuesObserver() {
        this.map = new HashMap<>();
    }

    private class QueueState {
        long summaryTime;
        int peopleGone;
    }

    public void observeTimeInQueue(Lifting current, List<Person> people) {
        QueueState queueState = map.get(current);
        if (queueState == null) {
            queueState = new QueueState();
        }
        queueState.peopleGone += people.size();
        for (var person : people) {
            queueState.summaryTime += ((long) TimeHolder.now() - person.getTimeStandToQueue());
        }
        map.put(current, queueState);
    }

    @Override
    public void printResult() {
        System.out.println("Average time in queue: ");
        map.forEach((key, value) -> {
            int from = key.getStartFloorNumber();
            int to = key.getDestinationFloorNumber();
            System.out.println(from + " -> " + to + " : " + (value.summaryTime + 0.) / value.peopleGone + " sec");
        });
    }
}
