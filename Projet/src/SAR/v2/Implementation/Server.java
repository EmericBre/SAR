package SAR.v2.Implementation;

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
				
			while (!mq.closed()) {
				byte[] message = mq.receive();
				if (message==null) {
					mq.close();
					break;
				}
				
				for (int j = 0; j < message.length; j++) {
					System.out.print((char)message[j]);
				}
				System.out.println("");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
//			e.printStackTrace();
			return;
		}
	}

}
