package info.kgeorgiy.java.advanced.implementor.full.interfaces;

import java.io.DataInput;
import java.io.Serializable;

/**
 * Inheritance hierarchy with plain covariant return types.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public final class CovariantReturns {
    public static final Class<?>[] OK = { Interface.class, Base.class, IntChild.class, LongChild.class };

    private CovariantReturns() {
    }

    public interface Interface extends DataInput {
        Serializable hello();
    }

    public static abstract class Base implements Interface {
        @Override
        public abstract Number hello();
    }

    public static abstract class IntChild extends Base {
        @Override
        public abstract Integer hello();
    }

    public static abstract class LongChild extends Base {
        @Override
        public final Long hello() {
            return null;
        }
    }
}
