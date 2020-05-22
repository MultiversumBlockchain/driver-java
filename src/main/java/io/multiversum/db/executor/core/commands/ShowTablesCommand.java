package io.multiversum.db.executor.core.commands;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

import io.multiversum.db.executor.core.CommandQueueExecutor;
import io.multiversum.db.executor.core.commands.results.CommandResult;
import io.multiversum.db.executor.core.commands.results.ResultRow;

public class ShowTablesCommand extends BaseSqlCommand {

	public ShowTablesCommand(SqlCommand parent) {
		super(parent);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public CommandResult run(CommandQueueExecutor executor) throws Exception {
		List<byte[]> tables = null;
		
		tables = executor.getContract().showTables().send();
		
		if (tables == null) {
			return result();
		}
		
		List<String> resultColumns = new ArrayList<String>();
		List<ResultRow> resultRows = new ArrayList<ResultRow>();
		
		resultColumns.add("tables");
		
		for (int i = 0; i < tables.size(); i += 2) {
			ByteBuffer wrapper = ByteBuffer.wrap(tables.get(i));
			
			String index = "" + wrapper.getLong();
			String name = new String(tables.get(i + 1)).trim();
			
			if (name.length() == 0) {
				continue;
			}
			
			List<String> columns = new ArrayList<String>();
			columns.add(name);
			
			resultRows.add(new ResultRow(index, columns));
		}
		
		return result().setResult(resultColumns, resultRows);
	}

}
