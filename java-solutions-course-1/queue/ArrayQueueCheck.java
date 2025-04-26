package queue;

public class ArrayQueueCheck {
    public static void fill(ArrayQueue queue, String prefix) {
        for (int i = 0; i < 5; i++) {
            queue.enqueue(prefix + i);
        }
    }

    public static void dump(ArrayQueue queue) {
        while (!queue.isEmpty()) {
            System.out.println("Size: " + queue.size() + " ; Element: " + queue.dequeue());
        }
    }

    public static void main(String[] args) {
        System.out.println("ArrayQueue test");
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        fill(queue1, "q1_");
        System.out.println("Queue 1 is filled");
        dump(queue1);
        System.out.println();
        fill(queue2, "q2_");
        System.out.println("Queue 2 is filled");
        dump(queue2);
    }
}
