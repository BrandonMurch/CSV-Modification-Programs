package OrganizeTags;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.*;

    public class OrganizeTags {
        private final HashMap<String, TagList> tags = new HashMap<>();
        private final List<Wine> wines = new ArrayList<>();

        public OrganizeTags() {
            initTagsMap();
        }

        public void initTagsMap() {
            String[] tagTitles = {"producer", "region", "grape", "country", "misc"};

            for(String tag : tagTitles) {
                tags.put(tag, new TagList());
            }
        }

        private void putTagsIntoCategories(String line) {
            String[] unsortedTags = line.split(",");
            String type = unsortedTags[0].toLowerCase();
            int arrayStartingPointAfterHeaders = 1;
            for(int i = arrayStartingPointAfterHeaders; i < unsortedTags.length; i++ ) {
                int finalI = i;
                tags.computeIfPresent(type, (s, tagList) -> {
                    tagList.add(unsortedTags[finalI]);
                    return tagList;
                });
            }
        }

        private void loadTagsFromFile() throws IOException {
            Scanner tags = new Scanner(Paths.get("tags-rows.csv"));

            while (tags.hasNext()) {
                putTagsIntoCategories(tags.nextLine());
            }

            tags.close();
        }

        private void putCorrectionsIntoCategories(String line) {
            String[] splitLine = line.split(",");
            if (splitLine.length == 3) {
                tags.computeIfPresent(splitLine[2], (key, tagList) -> {
                    tagList.add(splitLine[0], splitLine[1]);
                    return tagList;
                });
            }
        }

        private void loadCorrectionsFromFile() throws IOException {
            Scanner corrections = new Scanner(Paths.get("possible-tags-corrections.csv"));

            corrections.nextLine();

            while (corrections.hasNext()) {
                putCorrectionsIntoCategories(corrections.nextLine());
            }

            corrections.close();
        }

        private void addTagIntoWineByCategory(Wine wine, String currentTag) {
            currentTag = currentTag.trim();
            boolean tagFound = false;
            for(String type : tags.keySet()) {
                Optional<String> optionalTag = tags.get(type).getTagIfPresent(currentTag);
                if (optionalTag.isPresent()) {
                    wine.addTag(type, optionalTag.get());
                    tagFound = true;
                    break;
                }
            }
            if (!tagFound) {
                wine.addTag("notFound", currentTag);
            }
        }

        private Wine buildWineObject(String line) {
            Wine wine = new Wine();
            line = line.replaceAll("\"", "");
            String[] splitLine = line.split(",");
            for (int i = 0; i < splitLine.length; i++) {
                if (i == 0){
                    wine.setName(splitLine[0]);
                } else {
                    addTagIntoWineByCategory(wine, splitLine[i]);
                }
            }
            return wine;
        }

        private void loadWinesFromFile() throws IOException {
            Scanner wineList;
            wineList = new Scanner(Paths.get("wine-list.csv"));

            while(wineList.hasNext()) {
                Wine wine = buildWineObject(wineList.nextLine());
                wines.add(wine);
            }
        }

        public int getGreatestNumberOfTagsOfSpecifiedTypeInOneWine(String type) {
            int max = 0;
            for (Wine wine : wines) {
                int size = wine.getTags(type).size();
                if (size > max) {
                    max = size;
                }
            }
            return max;
        }


        private String getColumnHeaders(int numberOfGrapeColumns, int numberOfMiscColumns) {
            return "Name, Producer, Country, Region, Grapes" + ", ".repeat(numberOfGrapeColumns) + "Other" + ", ".repeat(numberOfMiscColumns) + "Unknown tags";
        }

        private void writeWinesToFile() {
            File output = new File("Wines-with-separated-tags.csv");
            PrintWriter writer;
            try {
                writer = new PrintWriter(output);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return;
            }
            int greatestNumberOfVarietals = getGreatestNumberOfTagsOfSpecifiedTypeInOneWine("grape");
            int greatestNumberOfMiscTags = getGreatestNumberOfTagsOfSpecifiedTypeInOneWine("misc");

            writer.println(getColumnHeaders(greatestNumberOfVarietals, greatestNumberOfMiscTags));

            wines.forEach(wine -> writer.println(wine.toCsvFormat(greatestNumberOfVarietals, greatestNumberOfMiscTags)));
            writer.close();
        }

        private void organize() {
            try {
                loadTagsFromFile();
                loadCorrectionsFromFile();
                loadWinesFromFile();
            } catch (IOException exception) {
                exception.printStackTrace();
                return;
            }

            writeWinesToFile();
        }

        public static void main(String[] args) {
            var organizeTags = new OrganizeTags();
            organizeTags.organize();
        }
}