package SAR.v3.Event;

import java.io.IOException;
import java.util.concurrent.locks.ReentrantLock;

import SAR.v3.Implementation.Channel;
import SAR.v3.Implementation.MessageQueue.Listener;

public class ReceiveEvent extends Event {
	
	Channel channel;
	Listener listener;
	private ReentrantLock receivelock;
	
	public ReceiveEvent(Executor executor, Channel channel, Listener listener) {
		super(executor);
		// TODO Auto-generated constructor stub
		this.receivelock = new ReentrantLock();
		this.channel = channel;
		this.listener = listener;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				receivelock.lock();
				byte[] bytes = new byte[64];
				
				try {
					int result = channel.read(bytes, 0, Integer.BYTES); // Récupère les 4 premiers octets pour savoir combien la taille du message à lire en octets.
					if (result != Integer.BYTES) { // Si le nombre d'octets lus n'est pas égal à 4, retourne une erreur.
						throw new IOException("Impossible to read message length");
					}
					int length= (bytes[0]<<24)&0xff000000|
						       (bytes[1]<<16)&0x00ff0000|
						       (bytes[2]<< 8)&0x0000ff00|
						       (bytes[3]<< 0)&0x000000ff; // Transforme la valeur en octets en une valeur entière.
					byte[] message = new byte[length]; // Crée un array de la bonne taille.
					result = channel.read(message, 0, length); // Lit le bon nombre d'octets pour récupérer le message.
					if (result == -1) { // Si le nombre d'octets lu n'est pas bon, sort de la méthode.
						receivelock.unlock();
						return;
					}
					receivelock.unlock();
					listener.received(message);	// Permet de recevoir le message.
					return;
				} catch (Exception e) {
					// TODO Auto-generated catch block
					receivelock.unlock();
					return;
				}
			}
		});
		thread.start();
	}

}
