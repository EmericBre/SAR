package SAR.v2.Implementation;

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
			
			while (!mq.closed()) {
				byte[] b = name.getBytes();
				mq.send(b, 0, b.length);
				System.out.print(""); // Résout le problème de fermeture de connection (bancal)
				if (mq.getChannelConnect()) {
					mq.close();
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}
	
	
}
