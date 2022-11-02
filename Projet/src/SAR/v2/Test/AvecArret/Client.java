package SAR.v2.Test.AvecArret;

import SAR.v2.Implementation.Manager;
import SAR.v2.Implementation.MessageQueueImpl;
import SAR.v2.Implementation.QueueBrokerImpl;
import SAR.v2.Implementation.Task;

public class Client extends Task {

	String name;
	QueueBrokerImpl broker;
	String toConnect;
	int port;
	
	public Client(String name, String toConnect, int port, Manager manager) {
		this.name = name;
		this.toConnect = toConnect;
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		MessageQueueImpl mq;
		try {
			mq = broker.connect(toConnect, port); // On crée une nouvelle connexion
			
			while (!mq.closed()) { // Tant que la connexion est ouverte, on écrit le nom.
				byte[] b = name.getBytes();
				mq.send(b, 0, b.length);
				Thread.sleep(1);
				mq.close(); // On ferme la connexion.
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
		
	}
	
	
}
