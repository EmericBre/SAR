package ASR;

import java.io.IOException;

public class Sender extends Task {
	
	Broker broker;
	
	Sender() {
		this.broker = new Broker("sender");
	}
	
	public void run() {
		Channel channel;
		try {
			channel = broker.connect("receiver", 8080);
			
			byte[] content = "test".getBytes();
			
			channel.write(content, 0, content.length);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
