package id.ac.ui.cs.advprog.eshop;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class EshopApplicationTests {

	@Test
	void contextLoads() {
		// This test verifies that the Spring application context loads correctly
		// The method is intentionally empty as the @SpringBootTest annotation handles the context loading verification
	}

	@Test
	void testMain() {
		// Verifies main method runs without errors
		EshopApplication.main(new String[] {});
	}

}
