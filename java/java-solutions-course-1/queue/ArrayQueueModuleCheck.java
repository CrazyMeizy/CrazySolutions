package queue;

public class ArrayQueueModuleCheck {
    public static void fill(String prefix) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueModule.enqueue(prefix + i);
        }
    }

    public static void dump() {
        while (!ArrayQueueModule.isEmpty()) {
            System.out.println("Size: " + ArrayQueueModule.size() + " ; Element: " + ArrayQueueModule.dequeue());
        }
    }

    public static void main(String[] args) {
        System.out.println("ArrayQueueModule test");
        fill("q1_");
        System.out.println("Queue 1 is filled");
        dump();
    }
}
