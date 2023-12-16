package kpi.zaranik.element;

import static kpi.zaranik.util.Constants.INF_TIME;

import java.util.SortedSet;
import java.util.TreeSet;
import kpi.zaranik.dto.Person;
import kpi.zaranik.element.visitor.Visitor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Floor implements Element {

    private int number;
    private final SortedSet<Person> people;
    private Lifting liftingToPrevFloor;
    private Lifting liftingToNextFloor;

    public Floor(int number) {
        this.number = number;
        this.people = new TreeSet<>();
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitFloor(this);
    }

    @Override
    public int getTimeNext() {
        if (people.isEmpty()) {
            return INF_TIME;
        }
        return people.first().getFinishWorkOnFloorTime();
    }

}
