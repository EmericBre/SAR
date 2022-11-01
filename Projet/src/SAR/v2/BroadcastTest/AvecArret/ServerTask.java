package SAR.v2.BroadcastTest.AvecArret;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class ServerTask extends Task{
	
	private HashMap<Integer, MessageQueueImpl> serveurqueues = new HashMap<>();
	
	QueueBrokerImpl broker;
	int port;
	
	public ServerTask(String name, int port, Manager manager) {
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
	}
	
	public void run() {
		
		for (int i = 0; i< 10; i++) {
			if (i%2==0) {
				MessageQueueImpl mq = null;
				try {
					mq = broker.accept(i);
				} catch(Exception e) {
				}
				serveurqueues.put(i, mq);
			}
		}
		for (int i = 0; i < 10; i++) {
			if (i%2==0) {
				byte[] message = null;
				try {
					message = serveurqueues.get(i).receive();
					synchronized(System.out) {
						System.out.print("Serveur lit : ");
						for (int j = 0; j < message.length; j++) {
							System.out.print((char)message[j]);
						}
						System.out.println("");
					}
				} catch(Exception e) {
				}
				for (HashMap.Entry<Integer, MessageQueueImpl> entry : serveurqueues.entrySet()) {
					if (entry.getKey() != i) {
						try {
							entry.getValue().send(message, 0, message.length);
						} catch(Exception e) {
						}
					}
				}
			}
		}
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = 0; i < 10; i++) {
			if(i%2==0) {
				serveurqueues.get(i).close();
				serveurqueues.remove(i);
			}
		}
	}

}