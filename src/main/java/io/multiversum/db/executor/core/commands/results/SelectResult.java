package io.multiversum.db.executor.core.commands.results;

import java.util.List;

import io.multiversum.db.executor.core.commands.CommandResult;
import io.multiversum.util.Pair;

public class SelectResult extends CommandResult<Pair<List<String>, List<List<String>>>> {

	public List<String> getAliases() {
		return result.getFirst();
	}
	
	public List<List<String>> getValues() {
		return result.getSecond();
	}
	
	public void setAliases(List<String> aliases) {
		if (result == null) {
			result = new Pair<List<String>, List<List<String>>>();
		}
		
		result.setFirst(aliases);
	}
	
	public void setValues(List<List<String>> values) {
		if (result == null) {
			result = new Pair<List<String>, List<List<String>>>();
		}
		
		result.setSecond(values);
	}
}
