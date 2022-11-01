package SAR.v2.BroadcastTest.AvecArret;

import SAR.v2.Implementation.*;

public class ClientTaskReceiver extends Task {

	String name;
	QueueBrokerImpl broker;
	int port;
	
	public ClientTaskReceiver(String name, int port, Manager manager) {
		this.name = name;
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		MessageQueueImpl mq = null;
		while (mq == null || !mq.closed()) {
			synchronized(ClientTaskSender.queues) {
				while(!ClientTaskSender.queues.containsKey(name)) {
					try {
						ClientTaskSender.queues.wait();
					} catch(Exception e) {
					}
				}
				mq = ClientTaskSender.queues.get(name);
			}
			
			try {
				byte[] message = mq.receive();
				if (message!=null) {
					synchronized(System.out) {
						System.out.print("Client " + this.name + " lit : ");
						for (int j = 0; j < message.length; j++) {
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
