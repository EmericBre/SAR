package SAR.v3.Implementation;

public class RDV {
	
	BrokerImpl accept;
	BrokerImpl connect;
	ChannelImpl channelaccept;

	public RDV(BrokerImpl accept) {
		this.accept = accept;
	}
	
	public synchronized ChannelImpl accept(int port) throws InterruptedException {
		while (connect == null) {
			wait();
		}
		
		while (channelaccept == null) {
			wait();
		}
		
		return channelaccept;
	}
	
	public synchronized ChannelImpl connect(int port) throws InterruptedException {
		this.channelaccept = new ChannelImpl();
		ChannelImpl channelconnect = new ChannelImpl();
		
		this.channelaccept.connectTo(channelconnect);
		channelconnect.connectTo(channelaccept);
		
		notifyAll();
		
		return channelconnect;
		
	}
	
	public void setBrokerConnect(BrokerImpl b) {
		this.connect = b;
	}
}
