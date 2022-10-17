package SAR.v3.Implementation;

import java.util.HashMap;

public class QueueBrokerImpl extends QueueBroker {
	
	private HashMap<Integer, MessageQueueImpl> mqs;
	private BrokerImpl broker;

	 QueueBrokerImpl(String name, Manager manager) {
		 super(name);
		 this.broker = new BrokerImpl(name, manager);
		 this.mqs = new HashMap<Integer, MessageQueueImpl>();
	 }
	 	 
	 MessageQueueImpl accept(int port) {
		 try {
			ChannelImpl channel = this.broker.accept(port);
			MessageQueueImpl mq = new MessageQueueImpl(channel);
			mqs.put(port, mq);
			return mq;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossible to connect.");
			e.printStackTrace();
			return null;
		}
	 }
	 
	 MessageQueueImpl connect(String name, int port) {
		 try {
			ChannelImpl channel = this.broker.connect(name, port);
			MessageQueueImpl mq = new MessageQueueImpl(channel);
			mqs.put(port, mq);
			return mq;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			System.out.println("Impossible to connect.");
			e.printStackTrace();
			return null;
		}
	 }

	@Override
	boolean bind(int port, AcceptListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean unbind(int port) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	boolean connect(String name, int port, ConnectListener listener) {
		// TODO Auto-generated method stub
		return false;
	}
	 
}
