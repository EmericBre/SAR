package ASR;

public class Broker {

	/**
	 * Constructeur pour créer un objet "conteneur", qui gèrera les différents threads et fera la liaison entre eux.
	 * @param name
	 */
	Broker(String name) {
	}
	
	/**
	 * Méthode permettant d'initialiser la connexion pour les threads "Receiver", à partir d'un port fourni.
	 * La méthode est bloquante tant que le Channel n'a pas été créé.
	 * Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	 * @param port
	 * @return
	 */
	Channel accept(int port) {
		return null;
	}
	
	/**
	 * Méthode permettant de créer un nouveau Channel d'échange de données entre deux tasks. Elle est destinée aux Tasks "Sender".
	 * Le Channel est créé via le port (pour garantir un accès unique) et un nom.
	 * Le booléen disconnected est initialisé à false (pour Channel) car la connexion est initialisée.
	 * @param name
	 * @param port
	 * @return
	 */
	Channel connect(String name, int port) {
		return null;
	}
}
