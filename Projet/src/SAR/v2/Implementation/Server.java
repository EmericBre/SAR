package SAR.v2.Implementation;

public class Server extends Task {
	
	QueueBroker broker;
	
	public Server(String name) {
		this.broker = new QueueBrokerImpl(name);
	}
	
	public void run() {
		MessageQueue mq;
		try {
			mq = broker.accept(8080); // On accepte la connexion
									
			while (!mq.closed()) {
				byte[] message = mq.receive();
				
				int length= (message[0]<<24)&0xff000000|
					       (message[1]<<16)&0x00ff0000|
					       (message[2]<< 8)&0x0000ff00|
					       (message[3]<< 0)&0x000000ff;
				for (int i = 4; i < 4+length; i++) {
					System.out.print((char)message[i]);
				}
				System.out.println();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return;
		}
	}

}
