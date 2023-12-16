package kpi.zaranik.model;

public class TimeHolder {

    private static int time;

    private TimeHolder(){}

    public static int getTime() {
        return time;
    }

    public static int now() {
        return getTime();
    }

    static void setTime(int newTime) {
        time = newTime;
    }
}
