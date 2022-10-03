package SAR.v2.Test;

import java.util.HashMap;

import SAR.v2.Implementation.Broker;
import SAR.v2.Implementation.Client;
import SAR.v2.Implementation.Server;

public class Test1Server {

	public static HashMap<String, Broker> BrokerManager = new HashMap<String, Broker>();

	public static void main(String[] args) throws InterruptedException {
		
		Client[] clients = new Client[10];
		Server serveur = new Server("server");
		
		for (int i = 0; i < 10; i++) {
			clients[i] = new Client(Integer.toString(i),"server");
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
