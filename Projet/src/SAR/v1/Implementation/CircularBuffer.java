package SAR.v1.Implementation;

public class CircularBuffer {
	
	int m_capacity;
	int m_start, m_end;
	byte m_elements[];

	CircularBuffer(int capacity) {
		m_capacity = capacity;
		m_elements = new byte[capacity];
		m_start = m_end = 0;
	}
	
	/**
	 * Vérifie l'état du buffer. 
	 * S'il est plein (que le nombre d'octets contenus dans le buffer est égal à sa capacité), alors la méthode retourne true (et il est donc impossible d'écrire dans le buffer). 
	 * Sinon, elle retourne false.
	 * @return
	 */
	boolean full() {
		int next = (m_end + 1) % m_capacity;
		return (next == m_start);
	}
	
	/**
	 * Vérifie l'état du buffer. 
	 * S'il est vide (que le nombre d'octets contenus dans le buffer est nul), alors la méthode retourne true (et il est donc impossible de lire dans le buffer). 
	 * Sinon, elle retourne false.
	 * @return
	 */
	boolean empty() {
		return (m_start == m_end);
	}
	
	/**
	 * La méthode permet d'ajouter un octet dans le buffer.
	 * Elle est synchronisée car on ne veut pas plusieurs ajouts en même temps de la part de différentes Tasks.
	 * @param bits
	 * @throws InterruptedException 
	 */
	void put(byte bits) throws InterruptedException { // on non-full buffer
		
		int next = (m_end + 1) % m_capacity;
		if (next == m_start)
			throw new IllegalStateException("Full");
		m_elements[m_end] = bits;
		m_end = next;
		
	}
	
	/**
	 * La méthode permet de lire un octet du buffer, puis de le supprimer du tableau.
	 * Elle est synchronisée car on ne veut pas plusieurs lecteurs en même temps, en simultané.
	 * @return
	 * @throws InterruptedException 
	 */
	byte get() throws InterruptedException { // on non-empty buffer
		if (m_start != m_end) {
			int next = (m_start + 1) % m_capacity;
			byte elem = m_elements[m_start];
			m_start = next;
			return elem;
		}
		throw new IllegalStateException("Empty");
	}
}
