package info.kgeorgiy.ja.chuprov.arrayset;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        ArraySet<Integer> set = new ArraySet<>(List.of(1,3,5,7,9));
        set.subSet(1,9);
    }
}
