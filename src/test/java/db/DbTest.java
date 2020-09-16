package db;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import people.Witness;

class DbTest {

	private final Witness w = new Witness("thaabit", "jacobs", "123456789");
	private final Db s = new Db();
	
	@Test
	void shouldAddWitnessToDb() {
		s.addWitness(w);
	}

}
