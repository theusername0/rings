package cc.redberry.rings.test;

import cc.redberry.rings.util.RandomDataGenerator;
import cc.redberry.rings.util.TimeUnits;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well44497b;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TestName;

import java.util.Objects;

/**
 * Abstract test class.
 *
 * @since 1.0
 */
public class AbstractTest {
    @Rule
    public TestName name = new TestName();

    @Before
    public void beforeMethod() throws Exception {
        if (getClass().getMethod(name.getMethodName()).isAnnotationPresent(TimeConsuming.class))
            Assume.assumeTrue(runTimeConsumingTests());
        if (getClass().getMethod(name.getMethodName()).isAnnotationPresent(Benchmark.class))
            Assume.assumeTrue(getClass().getMethod(name.getMethodName()).getDeclaredAnnotation(Benchmark.class).runAnyway() || runBenchmarks());
    }

    private static final RandomGenerator privateRandom = new Well44497b();
    private static final RandomDataGenerator privateRandomData = new RandomDataGenerator(privateRandom);

    protected static RandomDataGenerator getRandomData() {
        return privateRandomData;
    }

    /** Seeds random */
    protected static RandomGenerator getRandom() {
        synchronized (privateRandom) {
            privateRandom.setSeed(System.nanoTime());
            return privateRandom;
        }
    }

    protected static RandomGenerator getFixedRandom() {
        return privateRandom;
    }

    /**
     * Whether to run time consuming tests
     *
     * @return whether to run time consuming tests
     */
    public static boolean runTimeConsumingTests() {
        String runHard = System.getProperty("runLongTests");
        return Objects.equals(runHard, "") || Objects.equals(runHard, "true");
    }

    /**
     * Whether to run benchmarks
     *
     * @return whether to run benchmarks
     */
    public static boolean runBenchmarks() {
        String runBenchmarks = System.getProperty("runBenchmarks");
        return Objects.equals(runBenchmarks, "") ||
                Objects.equals(runBenchmarks, "true");
    }

    /**
     * Returns {@code nSmall} if time-consuming are disabled and {@code nLarge} otherwise
     */
    public static long its(long nSmall, long nLarge) {
        return runTimeConsumingTests() ? nLarge : nSmall;
    }

    /**
     * Returns {@code nSmall} if time-consuming are disabled and {@code nLarge} otherwise
     */
    public static int its(int nSmall, int nLarge) {
        return runTimeConsumingTests() ? nLarge : nSmall;
    }


    private volatile long lastTimestamp;

    public final void timestamp() {
        lastTimestamp = System.nanoTime();
    }

    public final void timeElapsed() {
        System.out.println(TimeUnits.nanosecondsToString(System.nanoTime() - lastTimestamp));
    }

//    @Test
//    public void testLT() throws Exception {
//        System.clearProperty("runLongTests");
//        Assert.assertEquals(10, its(10, 100));
//        System.setProperty("runLongTests", "");
//        Assert.assertEquals(100, its(10, 100));
//        System.clearProperty("runLongTests");
//    }
}
