package SAR.v1.Implementation;

public class Receiver extends Task {

	Broker broker;
	String toConnect;
	
	public Receiver(String name, String toConnect) {
		this.toConnect = toConnect;
		this.broker = new Broker(name);
	}
	
	public void run() {
		Channel channel;
		try {
			channel = broker.connect(toConnect, 8080); // On crée une nouvelle connexion
			
			byte[] bytes = new byte[1024];
			
			int length = channel.read(bytes, 0, 5); // On lit le message dans le buffer (on précise la taille du message à lire pour le moment)
			
			System.out.print("Message received : ");
			for (int i = 0; i < length; i++) {
				System.out.print((char)bytes[i]);
			}
			System.out.println();
			channel.disconnect(); // On ferme la connexion
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
