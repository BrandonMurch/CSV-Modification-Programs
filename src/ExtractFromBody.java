import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ExtractFromBody {

    private static String toCSVFormat(String name, String link, String linkText, String alcohol) {
        return name + ", " + ", " + link  + ", " + linkText + ", " + alcohol;
    }

    private static String getAlcohol(String body) {
        Pattern pattern = Pattern.compile("(Alcohol: )([\\d/.]+%)");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }
    
    private static String getLink(String body) {
        Pattern pattern = Pattern.compile("(href=\".*)(http.+)(\" target=)");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

    private static String getLinkText(String body) {
        Pattern pattern = Pattern.compile("(href=.+?>)([a-zA-Z0-9 -]+)(</a>)");
        Matcher matcher = pattern.matcher(body);
        if (matcher.find()) {
            return matcher.group(2);
        }
        return "";
    }

    private static String extractBodyToCSV(String body, String name) {
        String alcohol = getAlcohol(body);
        String link = getLink(body);
        String linkText = getLinkText(body);
        return toCSVFormat(name, link, linkText, alcohol);
    }

    private static String getNameThenExtractBodyToCSV(String line) {
        String[] body = line.split("\\t");
        if (body.length == 3) {
            return extractBodyToCSV(body[1], body[0]);
        }
        return "";
    }

    private static void loadInfoFromFileThenWriteToNewCSV() {
        Scanner scanner;
        PrintWriter writer;
        try {
            scanner = new Scanner(Paths.get("Title-Body-Image.tsv"));
            File outputFile = new File("Extracted-Information.csv");
            writer = new PrintWriter(outputFile);
        } catch (IOException exception) {
            exception.printStackTrace();
            return;
        }
        while(scanner.hasNext()) {
            String body = getNameThenExtractBodyToCSV(scanner.nextLine());
            writer.println(body);
        }
        writer.flush();
        writer.close();
    }

    public static void main(String[] args) {
        ExtractFromBody.loadInfoFromFileThenWriteToNewCSV();
    }

}
