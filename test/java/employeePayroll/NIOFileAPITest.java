package employeePayroll;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.FileHandler;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class NIOFileAPITest {
	
	private static String HOME = "D:\\Chandrakala\\EclipseWorkspace\\EmployeePayroll";//System.getProperty("user.home");
	private static String PLAY_WITH_NIO = "TemPlayGround";
	
	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {
		//check File Exists
		Path homePath = Paths.get(HOME);
		Assert.assertTrue(Files.exists(homePath));
		
		//Delete File and Check File Not Exist
		Path playPath = Paths.get(HOME + "/"+PLAY_WITH_NIO);
		if(Files.exists(playPath))
		{
			FileUtils.deleteDirectory(playPath.toFile());
		}
		Assert.assertTrue(Files.notExists(playPath));
		
		//create Directory
		Files.createDirectory(playPath);
		Assert.assertTrue(Files.exists(playPath));
		
		//create File
		IntStream.range(1,10).forEach(cntr ->{
			Path tempFile = Paths.get(playPath + "/temp"+cntr);
			Assert.assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			}
			catch(IOException e) {}
			Assert.assertTrue(Files.exists(tempFile));
		});
		
		//List Files,Directories as well as Files with Extension
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp"))
		.forEach(System.out::println);
	}
}
