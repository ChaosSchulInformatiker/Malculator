package malculator.shared;

import org.jetbrains.annotations.NotNull;

public interface Error {
    class SyntaxError implements Error {
        @NotNull
        public static SyntaxError INSTANCE = new SyntaxError();
        private SyntaxError() {}
    }
    class DividingByNullError implements Error {
        @NotNull
        public static DividingByNullError INSTANCE = new DividingByNullError();
        private DividingByNullError() {}
    }
    class NegativeNumberinRootsError implements Error {
        @NotNull
        public static NegativeNumberinRootsError INSTANCE = new NegativeNumberinRootsError();
        private NegativeNumberinRootsError() {}
    }
}
