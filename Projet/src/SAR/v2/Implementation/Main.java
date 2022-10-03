package SAR.v2.Implementation;

public class Main {

	public static void main(String[] args) {
				
		Server server = new Server("server");
		Client client = new Client("client", "server");
		
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
