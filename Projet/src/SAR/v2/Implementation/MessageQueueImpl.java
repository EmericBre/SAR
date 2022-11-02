package SAR.v2.Implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

public class MessageQueueImpl extends MessageQueue {
	
	private boolean closed;
	private ChannelImpl channel;
	private ReentrantLock sendlock;
	private ReentrantLock receivelock;

	MessageQueueImpl(ChannelImpl channel) {
		super();
		this.closed = false;
		this.channel = channel;
		this.receivelock = new ReentrantLock();
		this.sendlock = new ReentrantLock();
	}
	
	/**
	 * Méthode permettant d'envoyer un paquet d'octets à la channel distante.
	 * Appelle la méthode write de channel pour ajouter les bytes un à un dans le buffer.
	 * Utilise un lock pour assurer que plusieurs threads ne puissent pas envoyer en même temps.
	 */
	public void send(byte[] bytes, int offset, int length) {
		this.sendlock.lock();
		byte[] messageLength = ByteBuffer.allocate(Integer.BYTES).putInt(length).array(); // On transforme la longueur d'int à 4 octets, pour donner l'information au buffer.
		int counter = 0;
		try {
			counter = this.channel.write(messageLength, 0, Integer.BYTES); // On écrit la longueur du message. Cela permettra au lecteur de savoir combien d'octets il doit lire.
			counter += this.channel.write(bytes, 0, length); // Ensuite, on envoie le message à écrire.
			if (counter != length+Integer.BYTES) { // Si tout n'a pas pu être lu, on sort de la méthode.
				this.sendlock.unlock();
				return;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.sendlock.unlock();
			return;
		}
		this.sendlock.unlock();
	}
	
	/**
	 * Méthode permettant de recevoir un paquet d'octets de la channel distante.
	 * Appelle la méthode read de channel pour lire les bytes un à un dans le buffer.
	 * Utilise un lock pour assurer que plusieurs threads ne puissent pas lire en même temps.
	 */
	public byte[] receive() throws InterruptedException {
		this.receivelock.lock();
		byte[] bytes = new byte[64];
		
		try {
			int result = this.channel.read(bytes, 0, Integer.BYTES); // Récupère les 4 premiers octets pour savoir combien la taille du message à lire en octets.
			if (result != Integer.BYTES) { // Si le nombre d'octets lus n'est pas égal à 4, retourne une erreur.
				throw new IOException("Impossible to read message length");
			}
			int length= (bytes[0]<<24)&0xff000000|
				       (bytes[1]<<16)&0x00ff0000|
				       (bytes[2]<< 8)&0x0000ff00|
				       (bytes[3]<< 0)&0x000000ff; // Transforme la valeur en octets en une valeur entière.
			byte[] message = new byte[length]; // Crée un array de la bonne taille.
			result = this.channel.read(message, 0, length); // Lit le bon nombre d'octets pour récupérer le message.
			if (result == -1) { // Si le nombre d'octets lu n'est pas bon, sort de la méthode.
				this.receivelock.unlock();
				return null;
			}
			this.receivelock.unlock();
			return message;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			this.receivelock.unlock();
			return null;
		}
	}
	
	/**
	 * Méthode permettant de fermer la connexion entre deux Brokers.
	 */
	public void close() {
		this.channel.disconnect(); // Déconnecte la channel courante (la méthode disconnect déconnecte aussi la Channel affiliée)
		synchronized(channel) { // Libère la channel si elle était bloquée.
			channel.notifyAll();
		}
		ChannelImpl connectedTo = channel.getConnectedTo(); // Récupère la channel à laquelle elle est connectée pour s'assurer qu'elle puisse être également libérée.
		synchronized(connectedTo) {
			connectedTo.notifyAll();
		}
	}
	
	/**
	 * Retourne l'état de la channel, qui est le même que celui de la MessageQueue.
	 */
	public boolean closed() {
		return channel.disconnected();
	}
	
	/**
	 * Retourne la Channel associée à la MessageQueue.
	 * @return
	 */
	public ChannelImpl getChannel() {
		return channel;
	}
	
}
