package SAR.v3.Event;

import java.util.LinkedList;

public class Executor extends Thread {

	LinkedList<Event> events; // on stocke tous les events à traiter.
	
	public Executor() {
		this.events = new LinkedList<Event>();
	}
	
	public synchronized void run() {
		while(true) {
			if (events.isEmpty()) {
				try {
					wait(); // S'il n'y a pas d'events, on attend qu'un listener en trouve un pour passer à son exécution.
				} catch(InterruptedException e) {
				}
			}
			Event e = events.remove(0); // On le supprime de la liste des events.
			
			e.react(); // On effectue l'action demandée par cet event.
		}
	}
	
	/**
	 * Fonction permettant d'ajouter un nouvel event à la liste des events à exécuter.
	 * Fonction utilisable via n'importe quelle autre classe connaissant l'executor.
	 * Synchronisée pour éviter que plusieurs events soient ajoutés en même temps.
	 * @param e
	 */
	public synchronized void push(Event e) {
		events.add(e); 
		notify();
	}
}
