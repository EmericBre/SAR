package SAR.v2.Implementation;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

	public static HashMap<String, QueueBrokerImpl> BrokerManager = new HashMap<String, QueueBrokerImpl>();
	
	public HashMap<BrokerImpl, RDV> RdvManager= new HashMap<BrokerImpl, RDV>();
	
	public synchronized BrokerImpl getBroker(String name) throws InterruptedException {
		while(BrokerManager.get(name) == null) {
			wait();
		}
		return BrokerManager.get(name).getBroker();
	}
	
	public synchronized void addBroker(String name, QueueBrokerImpl b) {
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
	
	public ChannelImpl accept(QueueBrokerImpl accept, int port) throws InterruptedException {
		RDV rdv;
		synchronized(this) {
			rdv = new RDV(accept.getBroker());
			addRdv(accept.getBroker(), rdv);
			notifyAll();
		}
		ChannelImpl channelaccept = rdv.accept(port);
		
		return channelaccept;
	}
	
	public synchronized ChannelImpl connect(QueueBrokerImpl connect, String name, int port) throws InterruptedException {
		BrokerImpl BrokerToConnect = getBroker(name);
		
		while (getRDV(BrokerToConnect) == null) {
			wait();
		}
		
		RDV rdv = getRDV(BrokerToConnect);
		removeRDV(BrokerToConnect, rdv);
		
		rdv.setBrokerConnect(connect.getBroker());
		
		synchronized(rdv) {
			rdv.notifyAll();
		}
		
		ChannelImpl channelconnect = rdv.connect(port);
		
		return channelconnect;
	}
	
	public void freeUnusedPorts() {
		HashMap<QueueBrokerImpl, ArrayList<Integer>> toRemove = new HashMap<QueueBrokerImpl, ArrayList<Integer>>();
		for (HashMap.Entry<String, QueueBrokerImpl> entry : BrokerManager.entrySet()) {
			QueueBrokerImpl b = entry.getValue();
			ArrayList<Integer> ports = new ArrayList<Integer>();
			for (HashMap.Entry<Integer, MessageQueueImpl> entry2 : b.getMqs().entrySet()) {
				if (entry2.getValue().getChannel().disconnected()==true) {
					ports.add(entry2.getKey());
				}
			}
			if (!ports.isEmpty()) {
				toRemove.put(b, ports);
			}
		}
		for (HashMap.Entry<QueueBrokerImpl, ArrayList<Integer>> entry3 : toRemove.entrySet()) {
			for (Integer port : entry3.getValue()) {
				entry3.getKey().getMqs().remove(port);
				entry3.getKey().getBroker().open.remove(port);
			}
		}
	}
}
