import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Runnable Stream API drills. Read the README first, then modify these.
 */
public class StreamDrills {

    record Listing(String title, String category, int price) {}

    public static void main(String[] args) {
        List<String> words = List.of("java", "api", "test", "automation", "ci", "api");

        List<String> longUpper = words.stream()
                .filter(w -> w.length() > 3)
                .map(String::toUpperCase)
                .sorted()
                .toList();
        System.out.println(longUpper);

        Map<Integer, List<String>> byLength = words.stream()
                .collect(Collectors.groupingBy(String::length));
        System.out.println(byLength);

        Map<String, Long> counts = words.stream()
                .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
        System.out.println(counts);

        List<List<Integer>> nested = List.of(List.of(1, 2, 3), List.of(3, 4), List.of(5));
        int distinctSum = nested.stream()
                .flatMap(List::stream)
                .distinct()
                .mapToInt(Integer::intValue)
                .sum();
        System.out.println(distinctSum);

        List<Listing> listings = List.of(
                new Listing("Camera", "photo", 250),
                new Listing("Lens", "photo", 150),
                new Listing("Phone", "mobile", 500)
        );

        Map<String, Integer> totalByCategory = listings.stream()
                .collect(Collectors.groupingBy(
                        Listing::category,
                        Collectors.summingInt(Listing::price)));
        System.out.println(totalByCategory);

        String names = Arrays.asList("Ana", "Ben", "Cara").stream()
                .collect(Collectors.joining(", "));
        System.out.println(names);
    }
}
