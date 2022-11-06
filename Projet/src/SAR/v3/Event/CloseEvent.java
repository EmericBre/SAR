package SAR.v3.Event;

import SAR.v3.Implementation.ChannelImpl;
import SAR.v3.Implementation.Channel;
import SAR.v3.Implementation.MessageQueue.Listener;

public class CloseEvent extends Event{
	
	Channel channel;
	Listener listener;

	public CloseEvent(Executor executor, Channel channel, Listener listener) {
		super(executor);
		// TODO Auto-generated constructor stub
		this.channel = channel;
		this.listener = listener;
	}

	@Override
	public void react() {
		// TODO Auto-generated method stub
		this.channel.disconnect(); // Déconnecte la channel courante (la méthode disconnect déconnecte aussi la Channel affiliée)
		synchronized(channel) { // Libère la channel si elle était bloquée.
			channel.notifyAll();
		}
		ChannelImpl connectedTo = ((ChannelImpl)channel).getConnectedTo(); // Récupère la channel à laquelle elle est connectée pour s'assurer qu'elle puisse être également libérée.
		synchronized(connectedTo) {
			connectedTo.notifyAll();
		}
		
		Listener listenerConnected = ListenerManager.getInstance().remove(((ChannelImpl)channel).getConnectedTo()); // On arrête d'écouter sur le Listener où l'on est connecté. Il ne réceptionne plus d'actions.
		ListenerManager.getInstance().remove(channel); // On arrête d'écouter sur notre Listener. Il ne réceptionne plus d'actions.
		listener.closed(); // On ferme la connexion sur le listener.
		if (listenerConnected != null) {
			listenerConnected.closed(); // On ferme la connexion sur l'autre Listener.
		}
	}

}
