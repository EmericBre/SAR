package SAR.v2.BroadcastTest.SansArret;

import SAR.v2.Implementation.*;

public class ClientTaskReceiver extends Task {

	String name;
	QueueBroker broker;
	int port;
	
	public ClientTaskReceiver(String name, int port, Manager manager) {
		this.name = name;
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		MessageQueue mq = null;
		while (true) {
			synchronized(ClientTaskSender.queues) { // On synchronise la liste des MessageQueues actives dans ce test.
				while(!ClientTaskSender.queues.containsKey(name)) { // Si la MessageQueue à laquelle on veut se connecter n'est pas active, on attend.
					try {
						ClientTaskSender.queues.wait();
					} catch(Exception e) {
					}
				}
				mq = ClientTaskSender.queues.get(name); // On récupère la MessageQueue.
			}
			
			try {
				byte[] message = mq.receive();
				if (message!=null) {
					synchronized(System.out) { // On synchronise pour éviter que plusieurs threads puissent écrire en même temps.
						System.out.print("Client " + this.name + " lit : ");
						for (int j = 0; j < message.length; j++) { // On lit le message octet par octet.
							System.out.print((char)message[j]);
						}
						System.out.println("");
					}
				}
			} catch(Exception e) {
				return;
			}
		}
	}
}
