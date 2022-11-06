package SAR.v3.Implementation;

import java.io.IOException;

/**
 * Flux d'octets
 * @author emericb
 *
 */

public abstract class Channel {
	
	Channel() {}

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
	 * @throws InterruptedException 
	 */
	public abstract int read(byte[] bytes, int offset, int length) throws IOException, InterruptedException;
	
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
	public abstract int write(byte[] bytes, int offset, int length) throws IOException;
	
	/**
	 * Méthode permettant de vérifier si la Channel courante doit être gardée active ou non.
	 * Dépend de l'état du booléen disconnected. S'il est à true, plus aucune task ne tourne dessus, le Channel doit être détruit.
	 */
	public abstract void disconnect();
	
	/**
	 * Retourne un booléen true s'il n'y a plus aucune task active, false s'il en reste.
	 * @return
	 */
	public abstract boolean disconnected();
	
}
