package ASR;

public class Receiver extends Task {

	Broker broker;
	
	Receiver() {
		this.broker = new Broker("receiver");
	}
	
	public void run() {
		Channel channel;
		try {
			channel = broker.accept(8080);
			
			byte[] bytes = new byte[1024];
			
			int length = channel.read(bytes, 0, 5);
			
			System.out.print("Message received : ");
			for (int i = 0; i < length; i++) {
				System.out.print((char)bytes[i]);
			}
			System.out.println();
			channel.disconnect();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
}
