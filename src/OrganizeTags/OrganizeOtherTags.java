package OrganizeTags;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;

public class OrganizeOtherTags {

    private static void setTagCategoryInObject(String tag, TagsInCsvLine tags) {
        switch (tag) {
            case "Zero SO2" -> tags.setZeroSO2(tag);
            case "Pet-Nat" -> tags.setPetNat(tag);
            case "Danger" -> tags.setDanger(tag);
            case "Glou-glou", "Glou-Glou" -> tags.setGlou(tag);
            case "Oxidative" -> tags.setOxidative(tag);
            default -> tags.addOther(tag);
        }
    }

    private static TagsInCsvLine putTagsIntoCatagories(String nextLine) {
        String[] tagsLine = nextLine.split(",");
        TagsInCsvLine tagsInCsvLine = new TagsInCsvLine();
        for (String tags : tagsLine) {
            tags = tags.trim();
            setTagCategoryInObject(tags, tagsInCsvLine);
        }
        return tagsInCsvLine;
    }

    private static void loadTagsFromFileThenWriteToNewCSV() {
        Scanner tagsScanner;
        PrintWriter writer;
        try {
            tagsScanner = new Scanner(Paths.get("Other-tags-to-sort.csv"));
            File outputFile = new File("Sorted-Other-Tags.csv");
            writer = new PrintWriter(outputFile);
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        while(tagsScanner.hasNext()) {
            TagsInCsvLine tagsInCsvLine = putTagsIntoCatagories(tagsScanner.nextLine());
            writer.println(tagsInCsvLine.toCSVFormat());
        }

        writer.flush();
        writer.close();
        tagsScanner.close();
    }

    public static void main(String[] args) {
        loadTagsFromFileThenWriteToNewCSV();
    }
}
