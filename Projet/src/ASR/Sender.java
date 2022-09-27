package ASR;

import java.io.IOException;

public class Sender extends Task {
	
	Broker broker;
	
	public Sender(String name) {
		this.broker = new Broker(name);
	}
	
	public void run() {
		Channel channel;
		try {
			channel = broker.accept(8080); // On accepte la connexion
			
			byte[] content = ("test").getBytes();
			
			channel.write(content, 0, content.length); // On écrit un message dans le buffer
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
