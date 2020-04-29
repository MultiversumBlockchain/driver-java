package io.multiversum.db.executor.core.commands;

public class CommandResult<T> {

	public enum Status { Ok, Error }
	
	protected T result = null;
	protected Status status = Status.Ok;
	
	public CommandResult<T> setResult(T result) {
		this.result = result;
		
		return this;
	}
	
	public CommandResult<T> setStatus(Status status) {
		this.status = status;
		
		return this;
	}
	
	public boolean isOk() {
		return status == Status.Ok;
	}
	
	public T getResult() {
		return result;
	}
	
	public Status getStatus() {
		return status;
	}
	
}
