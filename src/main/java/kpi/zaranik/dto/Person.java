package kpi.zaranik.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@AllArgsConstructor
public class Person implements Comparable<Person> {

    private int finishWorkOnFloorTime;
    private int destinationFloorNumber;
    private int timeStandToQueue;

    @Override
    public int compareTo(Person person) {
        return this.finishWorkOnFloorTime - person.finishWorkOnFloorTime;
    }
}
