package OrganizeTags;

import java.util.*;

public class TagList {
    private HashSet<String> tags = new HashSet<>();
    private HashMap<String, String> correctedTags = new HashMap<>();

    public void add(String tag) {
        tags.add(tag);
    }

    public void add(String tag, String correctedTag) {
        correctedTags.put(tag, correctedTag);
    }

    public Optional<String> getTagIfPresent(String tag) {
        if (correctedTags.containsKey(tag)) {
            return Optional.of(correctedTags.get(tag));
        } else if (tags.contains(tag)) {
            return Optional.of(tag);
        } else {
            return Optional.empty();
        }
    }

    public void list() {
        tags.forEach(System.out::println);
        correctedTags.entrySet().forEach(System.out::println);
    }
}
