import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Stream;

public class Test {
    public static void main(String[] args) {
        ArrayList<String> ss = new ArrayList<>();
        ss.add("One");
        ss.add("Two");
        ss.add("Three");
        ss.add("Four");
        ss.add("Five");
        Stream<String> stringStream = ss.stream();
        List<String> asList = stringStream.collect(() -> {
                    return new ArrayList<String>();
                }, new BiConsumer<ArrayList<String>, String>() {
                    @Override
                    public void accept(ArrayList<String> strings, String e) {
                        strings.add(e);
                    }
                },
                new BiConsumer<ArrayList<String>, ArrayList<String>>() {
                    @Override
                    public void accept(ArrayList<String> strings1, ArrayList<String> c) {
                        strings1.addAll(c);
                    }
                });
    }
}
