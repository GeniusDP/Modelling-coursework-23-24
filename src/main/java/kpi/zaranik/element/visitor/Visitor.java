package kpi.zaranik.element.visitor;

import kpi.zaranik.element.Floor;
import kpi.zaranik.element.Generator;
import kpi.zaranik.element.Lifting;

public interface Visitor {

    void visitGenerator(Generator generator);

    void visitFloor(Floor floor);

    void visitLifting(Lifting lifting);

}
