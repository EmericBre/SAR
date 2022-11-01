package SAR.v2.Implementation;

public abstract class Broker {
	
	/**
	 * Constructeur pour créer un objet "conteneur", qui gèrera les différents threads et fera la liaison entre eux.
	 * @param name
	 */
	Broker(String name) {}
	
	/**
	 * Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un nouveau Channel pour une connexion entre deux Tasks.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	public abstract Channel accept(QueueBroker broker, int port) throws InterruptedException;
	
	/**
	 * Méthode permettant d'établir une connexion à un Channel déjà initialisée par le Broker courant. Il faut que le nom soit le même que celui du Broker, le port également.
	 * Le booléen disconnected est initialisé à false (pour Channel) car la connexion est initialisée.
	 * @param name
	 * @param port
	 * @return
	 * @throws InterruptedException 
	 */
	public abstract Channel connect(QueueBroker broker, String name, int port) throws InterruptedException;
}
