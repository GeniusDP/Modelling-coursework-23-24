package kpi.zaranik.model;

import java.util.Comparator;

import java.util.List;
import kpi.zaranik.element.Element;
import kpi.zaranik.element.visitor.TaskExecutionVisitor;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Modeller {

    private final int totalModellingTime;
    private final List<Element> elements;
    private final TaskExecutionVisitor taskExecutorVisitor;

    public void simulate() {
        while (TimeHolder.getTime() < totalModellingTime) {
//            System.out.println("New iteration: time now = " + TimeHolder.getTime());
            /*
                1. пробежаться по всем элементам и выбрать все с минимальным timeNext
                2. для всех выбранных вызвать визитор
            */
            int minimalTimeNext = elements.stream()
                .map(Element::getTimeNext)
                .min(Comparator.naturalOrder())
                .orElseThrow(IllegalStateException::new);
            TimeHolder.setTime(minimalTimeNext);

            // execute tasks for time now
            elements.stream()
                .filter(element -> element.getTimeNext() == TimeHolder.getTime())
                .forEach(element -> element.accept(this.taskExecutorVisitor));

        }
    }

}
