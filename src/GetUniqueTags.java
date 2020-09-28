import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Scanner;

public class GetUniqueTags {

	public static void main(String[] args) {
		HashSet<String> uniqueValues = new HashSet<>();
		Scanner file;

		try {
			file = new Scanner(Paths.get("LWS Master Wine Sheet - Possible Tags.csv"));
		} catch (IOException ex) {
			ex.printStackTrace();
			return;
		}

		while(file.hasNext()) {
			String line = file.nextLine();
			line = line.replaceAll("\"", "");
			String[] splitLine = line.split(",");
			for (String tag : splitLine) {
				if (!uniqueValues.contains(tag)) {
					uniqueValues.add(tag);
					System.out.println(tag);
				}
			}
		}
	}
}
