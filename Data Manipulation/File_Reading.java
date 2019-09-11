import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class File_Reading {
	public static ArrayList<String> FilesInDirectory(String Directory) {
		ArrayList<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(Database.basedir+Directory))) {

			result = (ArrayList<String>) walk.filter(Files::isRegularFile)
					.map(x -> x.toString()).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	public static ArrayList<String> FoldersInDirectoriy(String Directory){
		ArrayList<String> result = null;
		try (Stream<Path> walk = Files.walk(Paths.get(Database.basedir+Directory))) {

			result = (ArrayList<String>) walk.filter(Files::isDirectory)
					.map(x -> x.toString()).collect(Collectors.toList());

		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
}
