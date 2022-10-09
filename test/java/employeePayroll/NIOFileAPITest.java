package employeePayroll;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.FileHandler;
import java.util.stream.IntStream;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;

public class NIOFileAPITest {

	private static String HOME = "D:\\Chandrakala\\EclipseWorkspace\\EmployeePayroll";// System.getProperty("user.home");
	private static String PLAY_WITH_NIO = "TemPlayGround";

	@Test
	public void givenPathWhenCheckedThenConfirm() throws IOException {
		// check File Exists
		Path homePath = Paths.get(HOME);
		Assert.assertTrue(Files.exists(homePath));

		// Delete File and Check File Not Exist
		Path playPath = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		if (Files.exists(playPath)) {
			FileUtils.deleteDirectory(playPath.toFile());
		}
		Assert.assertTrue(Files.notExists(playPath));

		// create Directory
		Files.createDirectory(playPath);
		Assert.assertTrue(Files.exists(playPath));

		// create File
		IntStream.range(1, 10).forEach(cntr -> {
			Path tempFile = Paths.get(playPath + "/temp" + cntr);
			Assert.assertTrue(Files.notExists(tempFile));
			try {
				Files.createFile(tempFile);
			} catch (IOException e) {
			}
			Assert.assertTrue(Files.exists(tempFile));
		});

		// List Files,Directories as well as Files with Extension
		Files.list(playPath).filter(Files::isRegularFile).forEach(System.out::println);
		Files.newDirectoryStream(playPath).forEach(System.out::println);
		Files.newDirectoryStream(playPath, path -> path.toFile().isFile() && path.toString().startsWith("temp"))
				.forEach(System.out::println);
	}

	@Test
	public void givenADirectoryWhenWatchedListAllTheActivities() throws IOException {
		Path dir = Paths.get(HOME + "/" + PLAY_WITH_NIO);
		Files.list(dir).filter(Files::isRegularFile).forEach(System.out::println);
		new java8WatchServiceExample(dir).processEvent();
	}

	public class java8WatchServiceExample {
		private final Kind<?> ENTRY_CREATE = null;
		private final Kind<?> ENTRY_DELETE = null;
		private final Kind<?> ENTRY_MODIFY = null;
		private final WatchService watcher;
		private final Map<WatchKey, Path> dirWatchers;

		/* Creates a WatchService and registers the given directory */
		java8WatchServiceExample(Path dir) throws IOException {
			this.watcher = FileSystems.getDefault().newWatchService();
			this.dirWatchers = new HashMap<WatchKey, Path>();
			scanAndRegisterDirectories(dir);
		}

		/* Register the given directory with the watchServices; */
		private void registerDirWatchers(Path dir) throws IOException {
			WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
			dirWatchers.put(key, dir);

		}

		/*
		 * Register the given directory ,and all its sub-directories, with the
		 * WatchServices
		 */
		private void scanAndRegisterDirectories(final Path start) throws IOException {
			// register directory and sub-directories
			Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
				// @Override
				public FileVisitResult preVisitdirectory(Path dir, BasicFileAttributes attrs) throws IOException {
					registerDirWatchers(dir);
					return FileVisitResult.CONTINUE;
				}

			});
		}

		/* Process all events for keys queued to the watchers */
		@SuppressWarnings({ "rawtypes", "unchecked" })
		void processEvent() {
			while (true) {
				WatchKey key; // wait for key to be signalled
				try {
					key = watcher.take();
				} catch (InterruptedException x) {
					return;
				}
				Path dir = dirWatchers.get(key);
				if (dir == null)
					continue;
				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind kind = event.kind();
					Path name = ((WatchEvent<Path>) event).context();
					Path child = dir.resolve(name);
					System.out.format("%s: %s\n", event.kind().name(), child);// print out event

					// if directory is created ,then register it and its sub-directories
					if (kind == ENTRY_CREATE) {
						try {
							if (Files.isDirectory(child))
								scanAndRegisterDirectories(child);
						} catch (IOException x) {
						}
					} else if (kind.equals(ENTRY_DELETE)) {
						if (Files.isDirectory(child))
							dirWatchers.remove(key);
					}
				}
				// reset key and remove from set if directory no longer accsessible
				boolean valid = key.reset();
				if (!valid) {
					dirWatchers.remove(key);
					if (dirWatchers.isEmpty())
						break;// all directories are inaccessible
				}
			}
		}
	}
}
