import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SplitVintageAndCuvee {

		public static void main(String[] args) {
			List<String> years = new ArrayList<>();
			List<String> name = new ArrayList<>();

			Scanner file;

			try {
				file = new Scanner(Paths.get("vintages.csv"));
			} catch (IOException e) {
				e.printStackTrace();
				return;
			}

			File output = new File("output.csv");
			PrintWriter pw;
			try {
				pw = new PrintWriter(output);

			} catch (FileNotFoundException e) {
				e.printStackTrace();
				return;
			}

			String regex = "(\\d{4} )(.+)";
			Pattern p = Pattern.compile(regex);

			while(file.hasNext()) {
				String line = file.nextLine();
				line = line.replaceAll("\"", "");
				// put year into it's own list
				Matcher m = p.matcher(line);
				if (m.find()) {
					// group 1 - vintage, group 2 - cuvee

					String vintage = m.group(1).trim();
					System.out.println(vintage);
					String cuvee = escapeSpecialCharacters(m.group(2));
					System.out.println(cuvee);
					String joined = String.join(",", vintage, cuvee);
					System.out.println(joined);
					pw.println(joined);
				}

				else {
					pw.println("," + escapeSpecialCharacters(line));
				}
				// put cuvee into it's own list

			}
			file.close();
			pw.close();
		}

	public static String escapeSpecialCharacters(String data) {
		String escapedData = data.replaceAll("\\R", " ");
		if (data.contains(",") || data.contains("\"") || data.contains("'")) {
			data = data.replace("\"", "\"\"");
			escapedData = "\"" + data + "\"";
		}
		return escapedData;
	}

}
