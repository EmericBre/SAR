package SAR.v2.Implementation;

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

	public static HashMap<String, QueueBrokerImpl> BrokerManager = new HashMap<String, QueueBrokerImpl>(); // Liste stockant l'ensemble des Brokers actifs avec leur nom.
	
	public HashMap<BrokerImpl, RDV> RdvManager= new HashMap<BrokerImpl, RDV>(); // Liste stockant les RDV (création de connexion entre deux Brokers) avec le Broker demandant la connexion.
	
	/**
	 * Méthode retournant un Broker à partir de son nom.
	 * Si le Broker n'est pas dans la liste, il n'a donc pas été créé et le thread demandeur est placé en attente le tant qu'il soit créé.
	 * Méthode synchronisée.
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized BrokerImpl getBroker(String name) throws InterruptedException {
		while(BrokerManager.get(name) == null) {
			wait();
		}
		return BrokerManager.get(name).getBroker();
	}
	
	/**
	 * Méthode synchronisée pour ajouter le Broker à la liste.
	 * Permet d'assurer qu'aucun thread ne cherchera à récupérer un broker pendant l'ajout de celui-ci.
	 * @param name
	 * @param b
	 */
	public synchronized void addBroker(String name, QueueBrokerImpl b) {
		BrokerManager.put(name, b);
		notifyAll();
	}
	
	/**
	 * Méthode synchronisée permettant d'ajouter un nouveau RDV à la liste.
	 * Permet d'assurer qu'il n'y aura pas plusieurs ajouts/suppressions/requêtes effectuées en même temps sur la HashMap.
	 * @param accept
	 * @param rdv
	 */
	public synchronized void addRdv(BrokerImpl accept, RDV rdv) {
		RdvManager.put(accept, rdv);
	}
	
	/**
	 * Méthode synchronisée permettant de supprimer un RDV à la liste.
	 * Permet d'assurer qu'il n'y aura pas plusieurs ajouts/suppressions/requêtes effectuées en même temps sur la HashMap.
	 * @param accept
	 * @param rdv
	 */
	public synchronized void removeRDV(BrokerImpl accept, RDV rdv) {
		RdvManager.remove(accept, rdv);
	}
	
	/**
	 * Méthode synchronisée permettant de récupérer un RDV dans la liste.
	 * Permet d'assurer qu'il n'y aura pas plusieurs ajouts/suppressions/requêtes effectuées en même temps sur la HashMap.
	 * @param accept
	 * @return
	 */
	public synchronized RDV getRDV(BrokerImpl accept) {
		RDV rdv = RdvManager.get(accept);
		return rdv;
	}
	
	/**
	 * Méthode permettant de créer un RDV.
	 * On crée un nouveau RDV que l'on va ajouter à la liste des RDV, pour faire en sorte que l'autre Broker puisse se connecter à ce dernier.
	 * La méthode accept de rdv permettra de créer les channels.
	 * @param accept
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public ChannelImpl accept(QueueBrokerImpl accept, int port) throws InterruptedException {
		RDV rdv;
		synchronized(this) {
			rdv = new RDV(accept.getBroker()); // Nouveau RDV avec le Broker en paramètre
			addRdv(accept.getBroker(), rdv);
			notifyAll();
		}
		ChannelImpl channelaccept = rdv.accept(port); // Création des channels.
		
		return channelaccept;
	}
	
	/**
	 * Méthode permettant de récupérer un rdv existant pour se connecter.
	 * Retourne une channel après la création dans le RDV.
	 * @param connect
	 * @param name
	 * @param port
	 * @return
	 * @throws InterruptedException
	 */
	public synchronized ChannelImpl connect(QueueBrokerImpl connect, String name, int port) throws InterruptedException {
		BrokerImpl BrokerToConnect = getBroker(name); // On récupère le Broker auquel on veut se connecter.
		
		while (getRDV(BrokerToConnect) == null) { // Si le Broker cherché n'a pas encore créé de RDV, on attend.
			wait();
		}
		
		RDV rdv = getRDV(BrokerToConnect); // On récupère le RDV correspondant au Broker auquel se connecter.
		removeRDV(BrokerToConnect, rdv); // On supprime ce RDV de la liste car la connection est initialisée.
		
		rdv.setBrokerConnect(connect.getBroker()); // On dit au RDV qu'on est le Broker souhaitant se connecter.
		
		synchronized(rdv) {
			rdv.notifyAll();
		}
		
		ChannelImpl channelconnect = rdv.connect(port); // Création des channels.
		
		return channelconnect;
	}
	
	/**
	 * Méthode permettant de supprimer les connexions inactives.
	 * Dans le cas où on veut faire plusieurs connexions entre des Brokers sur le même port, il faut s'assurer que le port puisse être disponible s'il n'y a aucune connexion ouverte dessus.
	 * On regarde pour chaque Broker ses connexions actives, et on vérifie l'état du booléen disconnected du Channel.
	 */
	public synchronized void freeUnusedPorts() {
		HashMap<QueueBrokerImpl, ArrayList<Integer>> toRemove = new HashMap<QueueBrokerImpl, ArrayList<Integer>>();
		for (HashMap.Entry<String, QueueBrokerImpl> entry : BrokerManager.entrySet()) { // Pour chaque Broker enregistré.
			QueueBrokerImpl b = entry.getValue();
			ArrayList<Integer> ports = new ArrayList<Integer>();
			for (HashMap.Entry<Integer, MessageQueueImpl> entry2 : b.getMqs().entrySet()) { // Pour chaque MessageQueue du Broker courant.
				if (entry2.getValue().getChannel().disconnected()==true) { // On vérifie si la connexion de la channel est encore ouverte ou non.
					ports.add(entry2.getKey()); // Si elle ne l'est pas, on ajoute le port à la liste des ports à supprimer.
				}
			}
			if (!ports.isEmpty()) { // S'il y a des ports à supprimer.
				toRemove.put(b, ports); // On ajoute la liste des ports et le broker courant à la HashMap des connexions à supprimer.
			}
		}
		for (HashMap.Entry<QueueBrokerImpl, ArrayList<Integer>> entry3 : toRemove.entrySet()) { // Pour chaque connexion à supprimer.
			for (Integer port : entry3.getValue()) {
				entry3.getKey().getMqs().remove(port); // On supprime la MessageQueue de la liste des MessagesQueues du Broker.
				entry3.getKey().getBroker().open.remove(port); // On enlève la channel de la liste des connexions ouvertes du Broker.
			}
		}
	}
}
