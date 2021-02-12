package malculator.utils;

import java.util.function.Consumer;

public abstract class Either<A, B> {
    public static class First<A, B> extends Either<A, B> {
        public A value;
    }

    public static class Second<B, A> extends Either<A, B> {
        public B value;
    }

    public void either(Consumer<A> ifFirst, Consumer<B> ifSecond) {
        if (this instanceof First) {
            ifFirst.accept(((First<A, B>) this).value);
        } else {
            ifSecond.accept(((Second<B, A>) this).value);
        }
    }
}
