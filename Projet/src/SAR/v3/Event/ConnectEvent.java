package SAR.v3.Event;

import SAR.v3.Implementation.*;
import SAR.v3.Implementation.QueueBroker.ConnectListener;

public class ConnectEvent extends Event {
	
	String name;
	int port;
	ConnectListener listener;
	QueueBroker broker;

	public ConnectEvent(Executor executor, String name, int port, ConnectListener listener, QueueBroker broker) {
		super(executor);
		// TODO Auto-generated constructor stub
		this.name = name;
		this.port = port;
		this.listener = listener;
		this.broker = broker;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					if (((QueueBrokerImpl)broker).getMqs().containsKey(port)) { // On vérifie que la connexion ne soit pas déjà active. Si elle l'est, on finit la connexion avec la MessageQueue correspondante.
						listener.connected(broker.name, port, ((QueueBrokerImpl)broker).getMqs().get(port));	
					}
					ChannelImpl channel = ((QueueBrokerImpl)broker).getBroker().connect(broker, name, port);
					MessageQueueImpl mq = new MessageQueueImpl(channel, executor);
					((QueueBrokerImpl)broker).getMqs().put(port, mq);
					listener.connected(broker.name, port, mq); // Permet de se connecter.
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					System.out.println("Impossible to connect.");
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

}
