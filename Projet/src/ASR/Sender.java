package ASR;

import java.io.IOException;

public class Sender extends Task {
	
	Broker broker;
	
	Sender(Broker broker) {
		this.broker = broker;
	}
	
	public void run() {
		Channel channel = broker.connect("test", 8080);
		
		byte[] content = "test".getBytes();
		
		try {
			channel.write(content, 0, content.length);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		channel.disconnect();
	}

}
