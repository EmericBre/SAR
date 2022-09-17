package ASR;

import java.io.IOException;

public class Receiver extends Task {

	Broker broker;
	
	Receiver(Broker broker) {
		this.broker = broker;
	}
	
	public void run() {
		Channel channel = broker.accept(8080);
		
		byte[] bytes = new byte[1024];
		
		try {
			channel.read(bytes, 0, bytes.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		channel.disconnect();
	}
	
	
}
