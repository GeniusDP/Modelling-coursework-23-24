package kpi.zaranik;

import java.util.stream.IntStream;
import org.junit.jupiter.api.Test;

public class StreamTest {

    @Test
    void test() {
        IntStream.rangeClosed(1, 5).forEach(System.out::println);
    }

}
