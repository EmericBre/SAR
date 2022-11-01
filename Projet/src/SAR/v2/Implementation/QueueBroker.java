package SAR.v2.Implementation;

public abstract class QueueBroker {
	
	 QueueBroker(String name) {}
	 
	 public abstract MessageQueue accept(int port);
	 
	 public abstract MessageQueue connect(String name, int port);

}
