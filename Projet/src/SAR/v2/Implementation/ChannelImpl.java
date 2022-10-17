package SAR.v2.Implementation;

import java.io.IOException;

public class ChannelImpl extends Channel {

	private boolean disconnected;
	private ChannelImpl connectedTo;
	private CircularBuffer buffer;
	
	ChannelImpl() {
		super();
		this.disconnected = false;
		this.buffer = new CircularBuffer(64); // On crée un nouveau buffer (ici de taille 5).
	}
	
	/**
	 * Méthode permettant de lire une série de bytes.
	 * La méthode est bloquante, en attente de quelque chose à lire (elle ne peut pas lire si le buffer est vide).
	 * L'offset correspond à l'index de début de lecture, length correspond à la quantité de données lues (en bytes).
	 * Retourne un int pour donner le nombre d'octets lus (max = length).
	 * On lève une IOException dans le cas où il y a une erreur lors de la lecture des bytes, si la connexion coupe pendant la lecture.
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public int read(byte[] bytes, int offset, int length) throws IOException {
		int counter = 0;
		
		while (counter < bytes.length && counter < length) {
			try {
				while(this.buffer.empty() && !this.connectedTo.disconnected()) { // Tant que le buffer est vide, on attend que l'autre Task écrive
					synchronized(this) {
						this.wait();
					}
				}
				byte b = buffer.get();
				bytes[offset+counter] = b; // On récupère le byte du buffer.
				counter++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return -1;
			}
			synchronized(this.connectedTo) { // On notifie l'autre channel qu'une lecture a été faite (pour le réveiller si le buffer était plein)
				this.connectedTo.notifyAll();
			}
		}
		
		return counter;
	}
	
	/**
	 * Méthode permettant d'écrire une série de bytes
	 * La série de bytes écrite pourra ensuite être lue par une task.
	 * L'offset correspond à l'index de début d'écriture, length correspond à la quantité de données à écrire (en bytes).
	 * Retourne un int pour donner le nombre d'octets lus (max = length).
	 * On lève une IOException dans le cas où il y a une erreur lors de l'écriture des bytes, si la connexion coupe pendant l'écriture.
	 * Write peut être bloquante si le buffer est plein et qu'il n'y a plus la place pour écrire.
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 * @throws IOException
	 */
	public int write(byte[] bytes, int offset, int length) throws IOException {
		int counter = 0;
		
		while (counter < length && counter < bytes.length) {
			try {
				while(this.connectedTo.buffer.full() && !this.connectedTo.disconnected()) { // Tant que le buffer est plein, on attend que l'autre Task lise
					synchronized(this) {
						this.wait();
					}
				}
				this.connectedTo.buffer.put(bytes[offset+counter]); // On ajoute un byte dans le buffer
				counter++;
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				return -1;
			}
			synchronized(this.connectedTo) { // On notifie l'autre channel qu'une écriture a été faite (pour le réveiller si le buffer était vide)
				this.connectedTo.notifyAll();
			}
		}

		return counter;
	}
	
	/**
	 * Méthode permettant de vérifier si la Channel courante doit être gardée active ou non.
	 * Dépend de l'état du booléen disconnected. S'il est à true, plus aucune task ne tourne dessus, le Channel doit être détruit.
	 */
	public void disconnect() {
		this.disconnected = true;
		connectedTo.disconnected = true;
		synchronized(connectedTo) {
			notifyAll();
		}
	}
	
	/**
	 * Retourne un booléen true s'il n'y a plus aucune task active, false s'il en reste.
	 * @return
	 */
	public boolean disconnected() {
		return this.disconnected;
	}
	
	/**
	 * Permet de stocker la channel utilisée par la Task à laquelle on veut se connecter, pour faire la liaison entre elles.
	 * @param external
	 */
	public void connectTo(ChannelImpl external) {
		this.connectedTo = external;
	}
}
