import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * The #1 collections gotcha: the equals()/hashCode() contract.
 *
 * Run:  javac EqualsHashCodeDemo.java && java EqualsHashCodeDemo
 */
public class EqualsHashCodeDemo {

    // BROKEN: overrides equals but NOT hashCode.
    static class BrokenKey {
        final int id;
        BrokenKey(int id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            return o instanceof BrokenKey && ((BrokenKey) o).id == id;
        }
        // no hashCode() -> uses identity hash -> equal objects get different hashes
    }

    // CORRECT: both overridden.
    static class Key {
        final int id;
        Key(int id) { this.id = id; }

        @Override
        public boolean equals(Object o) {
            return o instanceof Key && ((Key) o).id == id;
        }
        @Override
        public int hashCode() {
            return Objects.hash(id);
        }
    }

    // MUTABLE key field that feeds hashCode -> dangerous as a map key.
    static class MutableKey {
        int id;
        MutableKey(int id) { this.id = id; }
        @Override public boolean equals(Object o) {
            return o instanceof MutableKey && ((MutableKey) o).id == id;
        }
        @Override public int hashCode() { return Objects.hash(id); }
    }

    // record gives equals()/hashCode()/toString() for free.
    record RecordKey(int id) { }

    public static void main(String[] args) {
        // ---- broken: equal objects, not found ----
        Map<BrokenKey, String> brokenMap = new HashMap<>();
        brokenMap.put(new BrokenKey(1), "a");
        System.out.println("BrokenKey equals      : " + new BrokenKey(1).equals(new BrokenKey(1)));
        System.out.println("BrokenKey hashCode    : "
                + new BrokenKey(1).hashCode() + " vs " + new BrokenKey(1).hashCode());
        System.out.println("brokenMap.get(new(1)) : " + brokenMap.get(new BrokenKey(1))
                + "   <- null! wrong bucket\n");

        // ---- correct ----
        Map<Key, String> map = new HashMap<>();
        map.put(new Key(1), "a");
        System.out.println("Key hashCode equal    : "
                + (new Key(1).hashCode() == new Key(1).hashCode()));
        System.out.println("map.get(new Key(1))   : " + map.get(new Key(1)) + "   <- found");

        Set<Key> set = new HashSet<>();
        set.add(new Key(1));
        set.add(new Key(1));                     // equal -> rejected
        System.out.println("set size after 2 adds : " + set.size());

        // ---- record as key ----
        Map<RecordKey, String> recMap = new HashMap<>();
        recMap.put(new RecordKey(7), "seven");
        System.out.println("record map.get        : " + recMap.get(new RecordKey(7)) + "\n");

        // ---- mutable key trap ----
        MutableKey mk = new MutableKey(5);
        Map<MutableKey, String> mutMap = new HashMap<>();
        mutMap.put(mk, "here");
        System.out.println("before mutation       : " + mutMap.get(mk));
        mk.id = 99;                              // change the field that feeds hashCode
        System.out.println("after mk.id = 99      : " + mutMap.get(mk)
                + "   <- lost, key is unreachable");
        System.out.println("values still holds it : " + mutMap.values());
    }
}
