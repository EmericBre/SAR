package SAR.v3.Event;

import java.util.concurrent.ConcurrentHashMap;

import SAR.v3.Implementation.Channel;
import SAR.v3.Implementation.MessageQueue;
import SAR.v3.Implementation.MessageQueue.Listener;

public class ListenerManager {

	static ListenerManager manager;
	
	private ConcurrentHashMap<Channel, Listener> map;
	
	private ListenerManager() {
		map = new ConcurrentHashMap<>();
	}
	
	/**
	 * Retourne l'instance de ListenerManager courante si elle existe.
	 * @return
	 */
	public synchronized static ListenerManager getInstance() {
		if (manager == null) {
			manager = new ListenerManager();
		}
		return manager;
	}
	
	/**
	 * Ajoute une nouvelle channel et son listener à la HashMap des Listeners.
	 * Méthode synchronisée pour ne pas avoir plusieurs exécutions en même temps sur la map.
	 * @param channel
	 * @param listener
	 */
	public synchronized void add(Channel channel, Listener listener) {
		map.put(channel,  listener);
		notifyAll();
	}
	
	/**
	 * Récupère le Listener lié à une channel.
	 * Méthode synchronisée pour ne pas avoir plusieurs exécutions en même temps sur la map.
	 * @param channel
	 * @return
	 */
	public synchronized Listener get(Channel channel) {
		Listener l = map.get(channel);
		while (l == null) {
			try {
				wait();
			} catch(InterruptedException e) {
			}
			l = map.get(channel);
		}
		return l;
	}
	
	/**
	 * Supprime un élément Channel/Listener dans la map.
	 * Méthode synchronisée pour ne pas avoir plusieurs exécutions en même temps sur la map.
	 * @param channel
	 * @return
	 */
	public synchronized Listener remove(Channel channel) {
		return map.remove(channel);
	}
}
