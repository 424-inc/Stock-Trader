import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

public class CSV {
	public static void MakeCSV(String directory,String Name, ArrayList<ArrayList<Double>> Data) {
		try {
			GenerateCSV(directory,Name,Data);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void GenerateCSV(String directory,String Name, ArrayList<ArrayList<Double>> Data) throws IOException{
		BufferedWriter writer = Files.newBufferedWriter(Paths.get(Database.basedir+"/AI/TrainingData/"+directory+"/"+Name+".csv"));
		@SuppressWarnings("resource")
		CSVPrinter csv = new CSVPrinter(writer, CSVFormat.DEFAULT.withSkipHeaderRecord());
		for(int i=0;i<Data.size();i++) {
				csv.printRecord(Data.get(i));
		}
		csv.flush();
	}

}
