import java.util.EnumMap;
import java.util.EnumSet;

public class EnumBasics {

    enum TestStatus {
        PASSED("P", true),
        FAILED("F", false),
        SKIPPED("S", false);

        private final String code;
        private final boolean blocksRelease;

        TestStatus(String code, boolean blocksRelease) {   // implicitly private
            this.code = code;
            this.blocksRelease = blocksRelease;
        }

        public String code() {
            return code;
        }

        public boolean blocksRelease() {
            return blocksRelease;
        }

        @Override
        public String toString() {
            return name().toLowerCase();
        }
    }

    public static void main(String[] args) {
        TestStatus s = TestStatus.FAILED;

        System.out.println(s == TestStatus.FAILED);         // identity is safe
        System.out.println(s.equals(TestStatus.FAILED));    // Enum does not override equals
        System.out.println(s.code());
        System.out.println(s);                              // toString
        System.out.println(s.name());                       // constant name
        System.out.println(s.ordinal());                    // declaration position
        System.out.println(TestStatus.values().length);

        System.out.println(TestStatus.valueOf("PASSED"));   // name, not toString
        try {
            TestStatus.valueOf("passed");
        } catch (IllegalArgumentException e) {
            System.out.println("valueOf is strict: " + e.getMessage());
        }

        String action = switch (s) {
            case FAILED -> "attach logs";
            case PASSED, SKIPPED -> "nothing";
        };
        System.out.println(action);

        System.out.println(EnumSet.of(TestStatus.FAILED));

        EnumMap<TestStatus, Integer> counts = new EnumMap<>(TestStatus.class);
        for (TestStatus t : TestStatus.values()) {
            counts.put(t, 0);
        }
        counts.merge(TestStatus.FAILED, 2, Integer::sum);
        System.out.println(counts);

        System.out.println(TestStatus.SKIPPED.ordinal());
    }
}
