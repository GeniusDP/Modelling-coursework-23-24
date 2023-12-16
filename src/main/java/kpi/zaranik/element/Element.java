package kpi.zaranik.element;

import kpi.zaranik.element.visitor.Visitor;

public interface Element {

    void accept(Visitor visitor);

    int getTimeNext();

}
