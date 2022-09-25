package ASR;

import java.util.HashMap;

public class Main {
	
	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) {
				
		Sender sender = new Sender();
		Receiver receiver = new Receiver();
		
		sender.start();
		receiver.start();
		
		try {
			sender.join();
			receiver.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
