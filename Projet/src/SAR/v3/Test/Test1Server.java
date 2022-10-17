package SAR.v3.Test;

import java.util.HashMap;

import SAR.v2.Implementation.*;

public class Test1Server {

	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) throws InterruptedException {
		
		Manager manager = new Manager();
		Client[] clients = new Client[10];
		Server serveur = new Server("server", 0, manager);
		
		for (int i = 0; i < 10; i++) {
			clients[i] = new Client(Integer.toString(i),"server", i, manager);
		}
		serveur.start();
		
		for (int i = 0; i < 10; i++) {
			clients[i].start();
		}
		serveur.join();
		
		for (int i = 0; i < 10; i++) {
			try {
				clients[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
