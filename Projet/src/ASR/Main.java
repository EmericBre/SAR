package ASR;

import java.util.HashMap;

public class Main {
	
	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) {
				
		Sender sender = new Sender("sender");
		Receiver receiver = new Receiver("receiver", "sender");
		
		receiver.start();
		sender.start();
		
		try {
			receiver.join();
			sender.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
