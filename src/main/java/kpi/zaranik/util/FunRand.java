package kpi.zaranik.util;

import java.util.Random;

public class FunRand {

    /**
     * Generates random value according to an exponential distribution
     *
     * @param mean the mean value
     * @return a random value according to an exponential distribution
     */
    public static int exp(int mean) {
        double a = 0;
        while (a == 0) {
            a = Math.random();
        }
        a = -mean * Math.log(a);

        return (int)a;
    }

    /**
     * Generates random value according to a uniform distribution
     *
     * @param min the minimum value of random value
     * @param max the maximum value of random value
     * @return a random value according to a uniform distribution
     */
    public static int uniform(int min, int max) {
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * Generates random value according to a normal (Gauss) distribution
     *
     * @param mean      the mean of random value
     * @param deviation the deviation of random value
     * @return a random value according to a normal (Gauss) distribution
     */
    public static int norm(int mean, int deviation) {
        var random = new Random();
        return (int)(mean + deviation * random.nextGaussian());
    }

}
