package info.kgeorgiy.java.advanced.implementor.advanced;

import info.kgeorgiy.java.advanced.implementor.full.interfaces.standard.DataInput;

/**
 * Classes with (finally-) overridden methods.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public enum WeirdInheritance {
    ;

    public static final Class<?>[] OK = {
            Abstract.class,
            Concrete.class,
            AbstractAgain.class,
            ConcreteAgain.class,
            Final.class
    };

    public static abstract class Abstract implements DataInput {
        public abstract void hello();
    }

    public static abstract class Concrete extends Abstract {
        @Override
        public void hello() {
        }
    }

    public static abstract class AbstractAgain extends Concrete {
        @Override
        public abstract void hello();
    }

    public static abstract class ConcreteAgain extends AbstractAgain {
        @Override
        public void hello() {
        }
    }

    public static abstract class Final extends ConcreteAgain {
        @Override
        public final void hello() {
        }
    }
}
