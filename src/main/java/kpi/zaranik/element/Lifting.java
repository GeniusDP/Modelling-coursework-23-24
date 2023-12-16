package kpi.zaranik.element;

import static kpi.zaranik.util.Constants.INF_TIME;

import java.util.ArrayList;
import java.util.List;
import kpi.zaranik.dto.Person;
import kpi.zaranik.element.visitor.Visitor;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Lifting implements Element {

    @Setter(AccessLevel.NONE)
    private int startFloorNumber;
    @Setter(AccessLevel.NONE)
    private int destinationFloorNumber;
    private int timeNext;
    private Floor destinationFloor;
    private Lifting nextLifting;
    private List<Person> peopleOnBoard;
    private List<Person> queue;

    public Lifting(int startFloorNumber, int destinationFloorNumber) {
        this.startFloorNumber = startFloorNumber;
        this.destinationFloorNumber = destinationFloorNumber;
        this.peopleOnBoard = new ArrayList<>();
        this.queue = new ArrayList<>();
        this.timeNext = INF_TIME;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitLifting(this);
    }

    @Override
    public int getTimeNext() {
        return timeNext;
    }

    public void appendQueue(Person newPerson) {
        queue.add(newPerson);
    }
}
