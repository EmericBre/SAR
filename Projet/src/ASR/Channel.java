package ASR;

import java.io.IOException;

/**
 * Flux d'octets
 * @author emericb
 *
 */

public class Channel {

	/**
	 * Méthode permettant de lire une série de bytes.
	 * La méthode est bloquante, en attente de quelque chose à lire.
	 * L'offset correspond à l'index de début de lecture, length correspond à la quantité de données lues (en bytes).
	 * Retourne un int pour donner le nombre d'octets lus.
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 * @throws IOException
	 */
	int read(byte[] bytes, int offset, int length) throws IOException {
		return 0;
	}
	
	/**
	 * Méthode permettant d'écrire une série de bytes
	 * La série de bytes écrite pourra ensuite être lue par une task "Receiver".
	 * L'offset correspond à l'index de début d'écriture, length correspond à la quantité de données à écrire (en bytes).
	 * Retourne un int pour donner le nombre d'octets lus.
	 * @param bytes
	 * @param offset
	 * @param length
	 * @return
	 * @throws IOException
	 */
	int write(byte[] bytes, int offset, int length) throws IOException {
		return 0;
	}
	
	/**
	 * Méthode permettant de vérifier si la Channel courante doit être gardée active ou non.
	 * Dépend de l'état du booléen disconnected. S'il est à true, plus aucune task ne tourne dessus, le Channel doit être détruit.
	 */
	void disconnect() {
	}
	
	/**
	 * Retourne un booléen true s'il n'y a plus aucune task active, false s'il en reste.
	 * @return
	 */
	boolean disconnected() {
		return false;
	}
}
