package SAR.v3.Implementation;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

	public static HashMap<String, BrokerImpl> BrokerManager = new HashMap<String, BrokerImpl>();
	
	public HashMap<BrokerImpl, RDV> RdvManager= new HashMap<BrokerImpl, RDV>();
	
	public synchronized BrokerImpl getBroker(String name) throws InterruptedException {
		while(BrokerManager.get(name) == null) {
			wait();
		}
		return BrokerManager.get(name);
	}
	
	public synchronized void addBroker(String name, BrokerImpl b) {
		BrokerManager.put(name, b);
		notifyAll();
	}
	
	public synchronized void addRdv(BrokerImpl accept, RDV rdv) {
		RdvManager.put(accept, rdv);
	}
	public synchronized void removeRDV(BrokerImpl accept, RDV rdv) {
		RdvManager.remove(accept, rdv);
	}
	
	public RDV getRDV(BrokerImpl accept) {
		RDV rdv = RdvManager.get(accept);
		return rdv;
	}
	
	public ChannelImpl accept(BrokerImpl accept, int port) throws InterruptedException {
		RDV rdv;
		synchronized(this) {
			rdv = new RDV(accept);
			addRdv(accept, rdv);
			notifyAll();
		}
		ChannelImpl channelaccept = rdv.accept(port);
		
		return channelaccept;
	}
	
	public synchronized ChannelImpl connect(BrokerImpl connect, String name, int port) throws InterruptedException {
		BrokerImpl BrokerToConnect = getBroker(name);
		
		while (getRDV(BrokerToConnect) == null) {
			wait();
		}
		
		RDV rdv = getRDV(BrokerToConnect);
		removeRDV(BrokerToConnect, rdv);
		
		rdv.setBrokerConnect(connect);
		
		synchronized(rdv) {
			rdv.notifyAll();
		}
		
		ChannelImpl channelconnect = rdv.connect(port);
		
		return channelconnect;
	}
}
