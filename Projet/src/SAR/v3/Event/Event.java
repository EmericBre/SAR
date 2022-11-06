package SAR.v3.Event;

public abstract class Event {

	Executor executor;
	
	public Event(Executor executor) {
		this.executor = executor;
	}
	
	public abstract void react();
}
