package Applications;

import java.io.IOException;
import java.nio.file.FileSystems;

import Models.TestDriver;

public class Program {
	public static void main(String[] args) throws IOException {
		System.out.println("Test is running in " + FileSystems.getDefault().getPath(".").toAbsolutePath());
		(new TestDriver()).Run();
	}
}
