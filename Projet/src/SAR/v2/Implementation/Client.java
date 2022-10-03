package SAR.v2.Implementation;

public class Client extends Task {

	String name;
	QueueBroker broker;
	String toConnect;
	
	public Client(String name, String toConnect) {
		this.name = name;
		this.toConnect = toConnect;
		this.broker = new QueueBrokerImpl(name);
	}
	
	public void run() {
		MessageQueue mq;
		try {
			mq = broker.connect(toConnect, 8080); // On crée une nouvelle connexion
			
//			while (true) {
				byte[] b = name.getBytes();
				mq.send(b, 0, b.length);
//			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
		
	}
	
	
}
