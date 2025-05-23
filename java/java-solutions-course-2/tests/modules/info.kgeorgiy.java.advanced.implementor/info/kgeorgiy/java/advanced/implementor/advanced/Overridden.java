package info.kgeorgiy.java.advanced.implementor.advanced;

/**
 * Classes with overridden methods.
 *
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
@SuppressWarnings("unused")
public enum Overridden {
    ;

    public static final Class<?>[] OK = {
            Base.class, Child.class, AllPublicChild.class,
            FinalGrandChild.class, NonFinalGrandChild.class,
            SuperMethods.class, $.class,
            PrivateSuper.class
    };

    public static final Class<?>[] FAILED = {
            PrivateClass.class, PrivateArg.class, PrivateResult.class
    };

    public static class Base {
        protected int protectedHello() {
            return 0;
        }
        public int publicHello() {
            return 0;
        }
        int packageHello() {
            return 0;
        }
    }

    @SuppressWarnings("AbstractMethodOverridesConcreteMethod")
    public static abstract class Child extends Base {
        @Override
        protected abstract int protectedHello();

        @Override
        public abstract int publicHello();

        @Override
        abstract int packageHello();
    }

    public static abstract class AllPublicChild extends Child {
        @Override
        public abstract int protectedHello();

        @Override
        public abstract int publicHello();

        @Override
        public abstract int packageHello();
    }

    public static abstract class FinalGrandChild extends Child {
        private final int value;

        public FinalGrandChild(final int value) {
            this.value = value;
        }

        @Override
        protected final int protectedHello() {
            return value;
        }

        @Override
        public final int publicHello() {
            return value;
        }

        @Override
        final int packageHello() {
            return value;
        }
    }

    public static abstract class NonFinalGrandChild extends Child {
    }

    @SuppressWarnings("MethodNameSameAsClassName")
    public static abstract class SuperMethods extends Child {
        SuperMethods(final SuperMethods methods) {
            System.out.println(methods);
        }
        abstract void SuperMethods(SuperMethods methods);
        abstract void Child(Child child);
        abstract void Base(Base base);
        abstract void NonFinalGrandChild(NonFinalGrandChild child);
    }

    public static abstract class $ {
        @SuppressWarnings("MethodNameSameAsClassName")
        abstract void $();
        abstract void $$();
        abstract void __();
    }

    public abstract static class PrivateSuper extends PrivateClass {
        public PrivateSuper(final int value) {
            super(value);
        }
    }

    private static abstract class PrivateClass {
        private final int value;

        public PrivateClass(final int value) {
            this.value = value;
        }

        public final int getValue() {
            return value;
        }

        abstract int hello();
    }

    public static abstract class PrivateArg {
        abstract int get(PrivateClass arg);
    }

    public static abstract class PrivateResult {
        abstract PrivateClass get();
    }
}
