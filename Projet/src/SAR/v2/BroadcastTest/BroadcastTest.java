package SAR.v2.BroadcastTest;

import SAR.v2.Implementation.*;

public class BroadcastTest {
	public static void main(String[] args) {
		
		Manager manager = new Manager();
		Task[] clients = new Task[10];
		ServerTask serveur = new ServerTask("serveur", 0, manager);
		serveur.start();
		
		for (int i = 0; i < 10; i++) {
			if (i%2 == 1) {
				clients[i] = new ClientTaskSender(Integer.toString(i),Integer.toString(i-1), i-1, manager);
			}
			else {
				clients[i] = new ClientTaskReceiver(Integer.toString(i),"serveur", i, manager);
			}
			clients[i].start();
		}
		for (int i = 0; i < 10; i++) {
			try {
				if (i==0) {
					serveur.join();
				}
				clients[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
