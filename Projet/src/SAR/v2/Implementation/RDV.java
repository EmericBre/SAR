package SAR.v2.Implementation;

public class RDV {
	
	BrokerImpl accept;
	BrokerImpl connect;
	ChannelImpl channelaccept;

	/**
	 * Création d'un RDV à partir du Broker demandant la connexion.
	 * @param accept
	 */
	public RDV(BrokerImpl accept) {
		this.accept = accept;
	}
	
	/**
	 * Méthode permettant de créer une channel et de la retourner pour le Broker acceptant.
	 * Synchronisée pour éviter que les deux Broker interagissent ensemble avec les fonctions accept et connect.
	 * Attend que l'autre Broker se soit connecté au RDV et que la channel a été créée.
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized ChannelImpl accept(int port) throws InterruptedException {
		while (connect == null) { // Attend l'autre Broker.
			wait();
		}
		
		while (channelaccept == null) { // Attend la création de la Channel.
			wait();
		}
		
		return channelaccept;
	}
	
	/**
	 * Méthode permettant de créer une channel et de la retourner pour le Broker connectant.
	 * Synchronisée pour éviter que les deux Broker interagissent ensemble avec les fonctions accept et connect.
	 * Crée les deux Channels et fait en sorte qu'elles soient interconnectées.
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized ChannelImpl connect(int port) throws InterruptedException {
		this.channelaccept = new ChannelImpl();
		ChannelImpl channelconnect = new ChannelImpl();
		
		this.channelaccept.connectTo(channelconnect); // Connecte les deux channels ensembles.
		channelconnect.connectTo(channelaccept);
		
		notifyAll(); // Notifie l'autre Broker que la Channel a été créée.
		
		return channelconnect;
		
	}
	
	/**
	 * Méthode permettant d'ajouter le 2ème Broker au RDV.
	 * @param b
	 */
	public void setBrokerConnect(BrokerImpl b) {
		this.connect = b;
	}
}
