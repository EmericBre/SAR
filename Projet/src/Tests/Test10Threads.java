package Tests;

import java.util.HashMap;

import ASR.*;

public class Test10Threads {
		
	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) {
		
		Sender[] senders = new Sender[5];
		Receiver[] receivers = new Receiver[5];
		
		for (int i = 0; i < 5; i++) {
			senders[i] = new Sender(Integer.toString(i));
			receivers[i] = new Receiver(Integer.toString(5+i), Integer.toString(i));
			senders[i].start();
			receivers[i].start();
			
			try {
				senders[i].join();
				receivers[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
