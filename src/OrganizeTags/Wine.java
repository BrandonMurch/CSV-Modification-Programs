package OrganizeTags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Wine {

    private final HashMap<String, List<String>> tags = new HashMap<>();
    private String name;

    public Wine() {
        String[] tagTypes = {"producer", "region", "country", "grape", "misc", "notFound"};
        for(String tagType : tagTypes) {
            tags.put(tagType, new ArrayList<>());
        }
    }

    public String toCsvFormat(int numberOfGrapeColumns, int numberOfMiscColumns) {
        List<String> grapes = tags.get("grape");
        List<String> misc = tags.get("misc");

        // if grapes are zero, grapes column is still automatically created
        // therefore, empty columns must be reduced by 1
        int emptyGrapeColumns = grapes.size() == 0 ? numberOfGrapeColumns - 1 : numberOfGrapeColumns - grapes.size();
        int emptyMiscColumns = misc.size() == 0 ? numberOfMiscColumns - 1 : numberOfMiscColumns - misc.size();


        String csvLine = name + "," + tags.get("producer") + "," + tags.get("country") + "," + tags.get("region") + "," + grapes + "," + ",".repeat(emptyGrapeColumns) +
                misc + "," + " ,".repeat(emptyMiscColumns) + tags.get("notFound");
        return csvLine.replaceAll("[\\[\\]]", "").replaceAll("null", "");
    }

    public void addTag(String type, String tag) {
        tags.computeIfPresent(type, (key, list) -> {
            list.add(tag.toLowerCase());
            return list;
        });
    }

    public String getName() {
        return name;
    }

    public List<String> getTags(String type) {
        return tags.get(type);
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getGrapes() {
        return this.tags.get("grapes");
    }

    public List<String> getMisc() {
        return this.tags.get("misc");
    }
}
