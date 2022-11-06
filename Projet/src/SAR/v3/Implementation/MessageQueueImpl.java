package SAR.v3.Implementation;

import java.util.concurrent.locks.ReentrantLock;

import SAR.v3.Event.*;

public class MessageQueueImpl extends MessageQueue {
	
	private Channel channel;
	private Executor executor;
	private Listener listener;

	public MessageQueueImpl(Channel channel, Executor executor) {
		super();
		this.channel = channel;
		this.executor = executor;
	}
	
	/**
	 * Méthode appelant l'event de fermeture de la connexion.
	 */
	public void close() {
		executor.push(new CloseEvent(executor, channel, listener));
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
		return (ChannelImpl)channel;
	}

	/**
	 * Ajout d'un listener sur la Channel pour réagir aux différentes actions.
	 */
	@Override
	public void setListener(Listener l) {
		// TODO Auto-generated method stub
		this.listener = l;
		ListenerManager.getInstance().add(channel, listener);
	}

	/**
	 * Méthode permettant d'envoyer une série d'octets
	 */
	@Override
	public boolean send(byte[] bytes) {
		// TODO Auto-generated method stub
		return send(bytes, 0, bytes.length);
	}

	/**
	 * Méthode remplaçant le send de la v2.
	 * Vérifie que la connexion est ouverte.
	 * Crée un event pour envoyer les données en paramètres.
	 */
	@Override
	public boolean send(byte[] bytes, int offset, int length) {
		// TODO Auto-generated method stub
		if (closed()) {
			return false;
		}
		executor.push(new SendEvent(executor, bytes, offset, length, channel, listener));
		return true;
	}
	
}
