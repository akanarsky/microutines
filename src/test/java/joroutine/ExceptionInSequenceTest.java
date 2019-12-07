package joroutine;

import org.junit.Test;

import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class ExceptionInSequenceTest {
    @Test(expected = RuntimeException.class)
    public void theTest() {
        Sequence<Integer> sequence = new Sequence<>(new Suspendable<SequenceScope<Integer>>() {
            @Override
            public void run(SequenceScope<Integer> scope) {
                scope.yield(100);
                scope.yield(200);
                throw new RuntimeException();
            }
        });

        for (Integer integer : sequence) {

        }
    }

    @Test(expected = RuntimeException.class)
    public void anotherTest() {
        Sequence<Integer> integers = new Sequence<>(new Suspendable<SequenceScope<Integer>>() {
            @Override
            public void run(SequenceScope<Integer> scope) {
                scope.yield(100);
                scope.yield(200);

                scope.yieldAll(new Sequence<>(new Suspendable<SequenceScope<Integer>>() {
                    @Override
                    public void run(SequenceScope<Integer> scope) {
                        scope.yield(220);
                        scope.yield(240);
                        throw new RuntimeException();
                    }
                }));

                scope.yield(300);
            }
        });

        for (Integer integer : integers) {

        }
    }
}
