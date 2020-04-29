package io.multiversum.db.executor.core.commands;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;

public class ShowTablesCommand extends BaseSqlCommand {

	public ShowTablesCommand(SqlCommand parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CommandResult<List<String>> run(CommandQueueExecutor executor) throws Exception {
		List<byte[]> tables = null;
		
		tables = executor.getContract().showTables().send();
		
		if (tables == null) {
			return this.<List<String>>result().setResult(new ArrayList<String>());
		}
		
		List<String> result = new ArrayList<String>();
		for (int i = 0; i < tables.size(); i += 2) {
			ByteBuffer wrapper = ByteBuffer.wrap(tables.get(i));
			
			String index = "" + wrapper.getLong();
			String name = new String(tables.get(i + 1)).trim();
			
			result.add(index);
			result.add(name);
		}
		
		return this.<List<String>>result().setResult(result);
	}

}
