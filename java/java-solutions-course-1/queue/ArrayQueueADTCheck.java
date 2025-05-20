package queue;

public class ArrayQueueADTCheck {
    public static void fill(ArrayQueueADT queue, String prefix) {
        for (int i = 0; i < 5; i++) {
            ArrayQueueADT.enqueue(queue,prefix + i);
        }
    }

    public static void dump(ArrayQueueADT queue) {
        while (!ArrayQueueADT.isEmpty(queue)) {
            System.out.println("Size: " + ArrayQueueADT.size(queue) + " ; Element: " + ArrayQueueADT.dequeue(queue));
        }
    }

    public static void main(String[] args) {
        System.out.println("ArrayQueueADT test");
        ArrayQueueADT queue1 =  ArrayQueueADT.create();
        ArrayQueueADT queue2 = ArrayQueueADT.create();
        fill(queue1, "q1_");
        System.out.println("Queue 1 is filled");
        dump(queue1);
        System.out.println();
        fill(queue2, "q2_");
        System.out.println("Queue 2 is filled");
        dump(queue2);
    }
}
