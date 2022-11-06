package SAR.v3.Event;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.locks.ReentrantLock;

import SAR.v3.Implementation.Channel;
import SAR.v3.Implementation.ChannelImpl;
import SAR.v3.Implementation.MessageQueue.Listener;

public class SendEvent extends Event{
	
	byte[] bytes;
	int offset;
	int length;
	Channel channel;
	Listener listener;
	private ReentrantLock sendlock;

	public SendEvent(Executor executor, byte[] bytes, int offset, int length, Channel channel, Listener listener) {
		super(executor);
		// TODO Auto-generated constructor stub
		this.sendlock = new ReentrantLock();
		this.bytes = bytes;
		this.offset = offset;
		this.length = length;
		this.channel = channel;
		this.listener = listener;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
		Thread thread = new Thread(new Runnable() {
			@Override
			public void run() {
				
				Channel connected = ((ChannelImpl)channel).getConnectedTo(); // On récupère la channel distante.
				Listener connectedListener = ListenerManager.getInstance().get(connected); // On récupère le listener correspondant à la channel.
				
				sendlock.lock();
				
				executor.push(new ReceiveEvent(executor, connected, connectedListener)); // On crée un nouvel event pour la réception du message par la channel distante.
				
				byte[] messageLength = ByteBuffer.allocate(Integer.BYTES).putInt(length).array(); // On transforme la longueur d'int à 4 octets, pour donner l'information au buffer.
				int counter = 0;
				try {
					counter = channel.write(messageLength, 0, Integer.BYTES); // On écrit la longueur du message. Cela permettra au lecteur de savoir combien d'octets il doit lire.
					counter += channel.write(bytes, 0, length); // Ensuite, on envoie le message à écrire.
					if (counter != length+Integer.BYTES) { // Si tout n'a pas pu être lu, on sort de la méthode.
						sendlock.unlock();
						return;
					}
					listener.sent(bytes, offset, length); // Permet d'envoyer le message. (pour le broadcast mais non implémenté)
				} catch (IOException e) {
					// TODO Auto-generated catch block
					sendlock.unlock();
					return;
				}
				sendlock.unlock();
			}
		});
		thread.start();
	}

}
