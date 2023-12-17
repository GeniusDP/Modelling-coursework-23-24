package kpi.zaranik.element;

import static kpi.zaranik.util.Constants.PERSON_GENERATION_MEAN_TIME;

import kpi.zaranik.element.visitor.Visitor;
import kpi.zaranik.util.FunRand;

public class Generator implements Element {

    private int timeNext;
    private boolean personGenerated;
    private final Lifting firstLifting;

    public Generator(Lifting firstLifting) {
        this.firstLifting = firstLifting;
        this.timeNext = 0;
        this.personGenerated = false;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visitGenerator(this);
        personGenerated = true;
    }

    @Override
    public int getTimeNext() {
        if (personGenerated) {
            timeNext = timeNext + FunRand.exp(PERSON_GENERATION_MEAN_TIME);
            personGenerated = false;
        }
        return timeNext;
    }

    public Lifting getFirstLifting() {
        return firstLifting;
    }
}
