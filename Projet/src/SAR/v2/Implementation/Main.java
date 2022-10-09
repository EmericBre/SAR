package SAR.v2.Implementation;

public class Main {

	public static void main(String[] args) {
				
		Manager manager = new Manager();
		Server server = new Server("server", 8080, manager);
		Client client = new Client("client", "server", 8080, manager);
		
		client.start();
		server.start();
		
		try {
			client.join();
			server.join();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
