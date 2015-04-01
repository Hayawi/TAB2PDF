import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.itextpdf.text.DocumentException;


public class ControllerTest {
	
	private static File file;
	private static Controller c;
	private static ComboBox<String> d;

	public static class AsNonApp extends Application {
	    @Override
	    public void start(Stage primaryStage) throws Exception {
	        // noop
	    }
	}

	@BeforeClass
	public static void initJFX() throws IOException {
	    Thread t = new Thread("JavaFX Init Thread") {
	        public void run() {
	            Application.launch(AsNonApp.class, new String[0]);
	        }
	    };
	    t.setDaemon(true);
	    t.start();
	    
	    
	    file = new File("testin.txt");
		file.createNewFile();
		PrintWriter f = new PrintWriter(file);
		f.write(
		"TITLE=Moonlight Sonata\n" +
		"SUBTITLE=Ludwig van Beethoven\n" +
		"SPACING=12\n\n" +
		
		"|-----s------------------------||--------p------|0-------------------------||| | \n" +
		"|-----1-----1----1p1----1------||-----<12>------||-----1-----1h2---1-----1-|||\n" +
		"|---2-----2-----2-----2<>------||*-----<11>---4*||*--2-----2-----2s12--2---|||\n" +
		"|-2-----2-----2-----2----------||*-----------12*||*2-----2-----2-----2-----|||\n" +
		"|-0----------------------------||---------------||-------------------------|||\n" +
		"|------------------------------||------h--------||-3-----------------------|||\n\n"	
		);
		f.close();
		
		c = new Controller();
		d = new ComboBox<String>();

		
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testOpenPDF() throws IOException {
		d.setValue("testin.txt");
		c.setChoosePDF(d);
		assertTrue(c.openPDF());

	}

	@Test
	public void testOpenFolder() throws IOException {
		d.setValue("testing/");
		c.setChoosePDF(d);
		assertTrue(c.openFolder());
	}

	@Test
	public void testBrowse() throws IOException, DocumentException {
	}
	/*
	@Test
	public void testSelectMultiple() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvertMultiple() {
		fail("Not yet implemented");
	}

	@Test
	public void testSelectFolder() {
		fail("Not yet implemented");
	}

	@Test
	public void testConvert() {
		fail("Not yet implemented");
	}

	@Test
	public void testShowMainMenu() {
		fail("Not yet implemented");
	}

	@Test
	public void testShowAdvancedMode() {
		fail("Not yet implemented");
	}

	@Test
	public void testPreview() {
		fail("Not yet implemented");
	}
*/
}
