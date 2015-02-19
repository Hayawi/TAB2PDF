package base_system;

import java.io.IOException;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class PreviewCreaterTest {
	
	String goodHeaderBody;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		goodHeaderBody = "TITLE=Moonlight Sonata\n"
				+ "SUBTITLE=Ludwig van Beethoven\n"
				+ "SPACING=5\n\n"

				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----1-----1-|-----1-----1-----1-----1-|\n"
				+"|---2-----2-----2-----2---|---2-----2-----2-----2---|\n"
				+"|-2-----2-----2-----2-----|-2-----2-----2-----2-----|\n"
				+"|-0-----------------------|-------------------------|\n"
				+"|-------------------------|-3-----------------------|\n\n"

				+"|-------------------------|-------------------------|\n"
				+"|-----1-----1-----3-----3-|-----3-----1-----0-----0-|\n"
				+"|---2-----2-----3-----3---|---1-----2-----2-----1---|\n"
				+"|-3-----3-----3-----3-----|-2-----2-----2-----0-----|\n"
				+"|-------------5-----------|-------------------------|\n"
				+"|-1-----------------------|-0-----------0-----------|";

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testWritePDFInMemory() throws IOException {
		PreviewCreater preview = new PreviewCreater();
		preview.writePDFInMemory(new Tablature(goodHeaderBody));
		//fail("Not yet implemented");
	}

}
