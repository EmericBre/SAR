package ASR;

public class Main {

	public static void main(String[] args) {
		
		Broker broker = new Broker("broker");
		
		Sender sender = new Sender(broker);
		Receiver receiver = new Receiver(broker);
		
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
