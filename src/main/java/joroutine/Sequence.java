package joroutine;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Sequence<T> implements Iterable<T> {
    private static final Object STOP = new Object();

    Object next = STOP;
    Continuation nextStep;
    Iterator<T> iterator;
    Iterator<T> nextIterator;

    public Sequence(Suspendable<SequenceScope<T>> suspendable) {
        this.nextStep = Magic.create(suspendable, new SequenceScope<>(this));
        this.iterator = new Iterator<T>() {
            @Override
            public boolean hasNext() {
                if (next == STOP) {
                    if (nextIterator != null) {
                        if (nextIterator.hasNext()) {
                            next = nextIterator.next();
                            return true;
                        } else {
                            nextIterator = null;
                        }
                    }
                    nextStep.run();
                    if (nextIterator != null)
                        return hasNext();
                }
                return next != STOP;
            }

            @Override
            public T next() {
                if (next == STOP) {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                }
                try {
                    return ((T) next);
                } finally {
                    next = STOP;
                }
            }
        };
    }

    @Override
    public Iterator<T> iterator() {
        return iterator;
    }


}
