package io.multiversum.db.executor.core;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import io.multiversum.db.executor.core.commands.CommandStack;

@RunWith(Parameterized.class)
public class ParserTest {

	private String sql;
	
	public ParserTest(String sql) {
		this.sql = sql;
	}
	
	@Parameters
	public static Collection<Object[]> data() {
		return Arrays.asList(Shared.Queries);
	}
	
	@Test
	public void testParse() {
		Parser.parse(this.sql);
		
		assertFalse(CommandStack.isCommandStackEmpty());
		
		CommandStack.clearAll();
	}

}
