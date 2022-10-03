package SAR.v2.Implementation;

public abstract class QueueBroker {
	
	 QueueBroker(String name) {}
	 
	 abstract MessageQueue accept(int port);
	 
	 abstract MessageQueue connect(String name, int port);

}
