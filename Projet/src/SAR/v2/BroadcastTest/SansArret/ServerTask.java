package SAR.v2.BroadcastTest.SansArret;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class ServerTask extends Task{
	
	private HashMap<Integer, MessageQueue> serveurqueues = new HashMap<>();
	
	QueueBroker broker;
	int port;
	Manager manager;
	
	public ServerTask(String name, int port, Manager manager) {
		this.broker = new QueueBrokerImpl(name, manager);
		this.port = port;
		this.manager = manager;
	}
	
	public void run() {
		
		while(true) {
			this.manager.freeUnusedPorts();
			
			for (int i = 0; i< 10; i++) {
				if (i%2==0) {
					MessageQueue mq = null;
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
					for (HashMap.Entry<Integer, MessageQueue> entry : serveurqueues.entrySet()) {
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

}
