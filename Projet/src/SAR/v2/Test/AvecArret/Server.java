package SAR.v2.Test.AvecArret;

import SAR.v2.Implementation.Manager;
import SAR.v2.Implementation.MessageQueue;
import SAR.v2.Implementation.QueueBroker;
import SAR.v2.Implementation.QueueBrokerImpl;
import SAR.v2.Implementation.Task;

public class Server extends Task {
	
	QueueBroker broker;
	int port;
	
	public Server(String name, int port, Manager manager) {
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		MessageQueue mq;
		try {
			mq = broker.accept(port); // On accepte la connexion
				
			while (!mq.closed()) { // Tant que la connexion est ouverte, on lit le message.
				byte[] message = mq.receive();
				if (message==null) { // Si le message n'a pas été lu, on ferme la connexion et on sort de la boucle.
					mq.close();
					break;
				}
				
				for (int j = 0; j < message.length; j++) { // On lit le message octet par octet.
					System.out.print((char)message[j]);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			return;
		}
	}

}
