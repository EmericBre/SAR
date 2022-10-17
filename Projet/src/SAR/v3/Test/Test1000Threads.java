package SAR.v3.Test;

import java.util.HashMap;

import SAR.v2.Implementation.Broker;
import SAR.v2.Implementation.Client;
import SAR.v2.Implementation.Manager;
import SAR.v2.Implementation.Server;

public class Test1000Threads {

	public static void main(String[] args) {
		
		Manager manager = new Manager();
		Client[] clients = new Client[500];
		Server[] serveurs = new Server[500];
		
		for (int i = 0; i < 500; i++) {
			serveurs[i] = new Server(Integer.toString(500+i), i, manager);
			clients[i] = new Client(Integer.toString(i),Integer.toString(500+i), i, manager);
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
