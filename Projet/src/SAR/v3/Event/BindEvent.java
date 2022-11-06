package SAR.v3.Event;

import java.util.HashMap;

import SAR.v3.Implementation.*;
import SAR.v3.Implementation.QueueBroker.AcceptListener;

public class BindEvent extends Event{
	
	int port;
	AcceptListener listener;
	QueueBroker broker;
	Manager manager;

	public BindEvent(Executor executor, int port, AcceptListener listener, QueueBroker broker, Manager manager) {
		super(executor);
		// TODO Auto-generated constructor stub
		this.port = port;
		this.listener = listener;
		this.broker = broker;
		this.manager = manager;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				manager.freeUnusedPorts(); // On supprime les connexions inactives pour permettre de les recréer.
				try {
					ChannelImpl channel = ((QueueBrokerImpl)broker).getBroker().accept(broker, port); // On accepte la connexion via Broker.
					MessageQueueImpl mq = new MessageQueueImpl(channel, executor);
					((QueueBrokerImpl)broker).getMqs().put(port, mq);
					listener.accepted(port, mq); // Permet d'accepter une connexion.
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
