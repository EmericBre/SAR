package ASR;

public class CircularBuffer {
	
	private int bufferSize;
	private byte[] buffer;
	private int nextPlace;
	private int nextLeaver;
	private int nBits = 0;
	private int totalBitsStored = 0;
	private int treatedBits = 0;

	CircularBuffer(int capacity) {
		this.bufferSize = capacity;
		this.buffer = new byte[bufferSize];
		this.nextPlace = 0;
		this.nextLeaver = 0;
		this.nBits = 0;
	}
	
	/**
	 * Vérifie l'état du buffer. 
	 * S'il est plein (que le nombre d'octets contenus dans le buffer est égal à sa capacité), alors la méthode retourne true (et il est donc impossible d'écrire dans le buffer). 
	 * Sinon, elle retourne false.
	 * @return
	 */
	boolean full() {
		return this.nBits == bufferSize;
	}
	
	/**
	 * Vérifie l'état du buffer. 
	 * S'il est vide (que le nombre d'octets contenus dans le buffer est nul), alors la méthode retourne true (et il est donc impossible de lire dans le buffer). 
	 * Sinon, elle retourne false.
	 * @return
	 */
	boolean empty() {
		return this.nBits == 0;
	}
	
	/**
	 * La méthode permet d'ajouter un octet dans le buffer.
	 * Elle est synchronisée car on ne veut pas plusieurs ajouts en même temps de la part de différentes Tasks.
	 * @param bits
	 * @throws InterruptedException 
	 */
	void put(byte bits) throws InterruptedException { // on non-full buffer
		
		this.buffer[nextPlace] = bits; // On ajoute le message à la prochaine place disponible
		this.nextPlace = (nextPlace + 1)%bufferSize; // On décale l'index de la prochaine place disponible. Si on atteint la fin du buffer, on revient au début (d'où le modulo)
		this.nBits ++; // On incrémente la variable qui stocke le nombre de messages dans le buffer à cet instant.
		
	}
	
	/**
	 * La méthode permet de lire un octet du buffer, puis de le supprimer du tableau.
	 * Elle est synchronisée car on ne veut pas plusieurs lecteurs en même temps, en simultané.
	 * @return
	 * @throws InterruptedException 
	 */
	byte get() throws InterruptedException { // on non-empty buffer
		byte bits = 0;
		
		bits = this.buffer[nextLeaver]; // On récupère le message le plus ancien du buffer (le premier à dépiler)
		this.buffer[nextLeaver] = 0; 
		this.nextLeaver = (this.nextLeaver + 1)%bufferSize; // On décale l'index du prochain message à dépiler. Si on atteint la fin du buffer, on revient au début (d'où le modulo)
		this.nBits --; // On décrémente la variable qui stocke le nombre de messages dans le buffer à cet instant.

		return bits;
	}
}
