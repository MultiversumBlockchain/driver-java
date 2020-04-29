package io.multiversum.db.executor.core.commands;

import java.util.ArrayList;
import java.util.EmptyStackException;
import java.util.Stack;

public class CommandStack {

	private static final ArrayList<SqlCommand> commands = new ArrayList<SqlCommand>();
	private static final Stack<Object> results = new Stack<Object>();
	
	public static SqlCommand pop() {
		try {
			return commands.remove(0);
		} catch (IndexOutOfBoundsException e) {
			return null;
		}
	}
	
	public static void push(SqlCommand command) {
		commands.add(command);
	}
	
	public static void pushResult(Object result) {
		results.push(result);
	}
	
	public static Object popResult() {
		try {
			return results.pop();
		} catch (EmptyStackException e) {
			return null;
		}
	}
	
	public static boolean isCommandStackEmpty() {
		return commands.isEmpty();
	}
	
	public static boolean isResultStackEmpty() {
		return results.isEmpty();
	}
	
	public static void clearAll() {
		commands.clear();
		results.clear();
	}
	
	private CommandStack() {}
	
}
