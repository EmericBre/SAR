package SAR.v3.Event.Test;

import java.util.LinkedList;
import java.util.List;

import SAR.v3.Event.Executor;
import SAR.v3.Implementation.Manager;

public class Main {
	
	static int nbClient = 6;
	static int portServer = 0;
	static int capacity = 64;
	
	public static void main(String[] args) throws InterruptedException {
		Executor executor = new Executor();
		Manager manager = new Manager();
		
		Server server = new Server("serveur", portServer, capacity, nbClient, executor, manager);
		server.start();
		
//		List<Server> servers = new LinkedList<>();
		List<Client> clients = new LinkedList<>();
		for (int i=0; i<nbClient; i++) {
//			Server server = new Server(Integer.toString(nbClient+i), portServer, capacity, nbClient, executor, manager);
//			servers.add(server);
//			server.run();
			Client client = new Client(Integer.toString(i), "serveur", i, capacity, executor, nbClient, manager);
			clients.add(client);
			client.start();
		}
		
		executor.start();
	}

}
