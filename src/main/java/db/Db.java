package db;

import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import people.Witness;

public class Db {
	
	private final String jdbiURL = "jdbc:postgres:file:./trackerdb";
	private final Jdbi jdbi = Jdbi.create(jdbiURL, "postgres", "codex123");
	
	private final Handle handle =jdbi.open();
	
	public void addWitness(Witness witness)
	{
		handle.execute("insert into witness (first_name, last_name, contact) values (?, ?, ?)", witness.getFirstName(), witness.getLastName(), witness.getContact());
	}
	
	public void clearTable()
	{
		handle.execute("delete from witness");
	}
	
	public static void main(String[] args)
	{
		Db s = new Db();
		Witness w = new Witness("thaabit", "jacobs", "123456789");
		
		s.addWitness(w);
	}
	
}
