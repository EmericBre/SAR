package SAR.v2.Test.AvecArret;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class Test10Threads {
		
	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) {
		
		Manager manager = new Manager();
		Client[] clients = new Client[5];
		Server[] serveurs = new Server[5];
		
		for (int i = 0; i < 5; i++) {
			serveurs[i] = new Server(Integer.toString(5+i), i, manager);
			clients[i] = new Client(Integer.toString(i),Integer.toString(5+i), i, manager);
			serveurs[i].start();
			clients[i].start();
			
			try {
				clients[i].join();
				serveurs[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
