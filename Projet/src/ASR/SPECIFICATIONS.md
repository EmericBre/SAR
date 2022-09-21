Deuxième version de la spécification :

Channel :

Classe permettant d'effectuer une communication entre deux tâches, pour la transaction de données. 
Un Channel est créé par un Broker (méthode accept), et il est possible de créer d'autres instances d'un Channel sémantiquement identique. 
Ici, on transfère des octets dans un buffer, donc on est dans le cas d'un flux d'octet FIFO sans perte où l'on a la garantie de recevoir les octets dans la TaskB dans le même ordre que la TaskA.


Read :

	Méthode permettant de lire une série de bytes.
	La méthode est bloquante, en attente de quelque chose à lire (elle ne peut pas lire si le buffer est *	
	vide).
	L'offset correspond à l'index de début de lecture, length correspond à la quantité de données lues (en 
	bytes).
	Retourne un int pour donner le nombre d'octets lus (max = length).
	On lève une IOException dans le cas où il y a une erreur lors de la lecture des bytes, si la connexion 
	coupe pendant 	la lecture.
	
Write :

	Méthode permettant d'écrire une série de bytes
	La série de bytes écrite pourra ensuite être lue par une task "Receiver".
	L'offset correspond à l'index de début d'écriture, length correspond à la quantité de données à écrire 
	(en bytes).
	Retourne un int pour donner le nombre d'octets lus (max = length).
	On lève une IOException dans le cas où il y a une erreur lors de l'écriture des bytes, si la connexion 
	coupe pendant 	l'écriture.
	Write peut être bloquante si le buffer est plein et qu'il n'y a plus la place pour écrire.
	
Disconnect :

	Méthode permettant de vérifier si la Channel courante doit être gardée active ou non.
	Dépend de l'état du booléen disconnected. S'il est à true, plus aucune task ne tourne dessus, le Channel 
	doit être détruit.
	
Disconnected : 

	Retourne un booléen true s'il n'y a plus aucune task active, false s'il en reste.
	

Si la connexion à une TaskA est fermée pendant que la TaskB est en train d'écrire, la TaskB retourne une IOException et disconnected passe à true.
Si la connexion à une TaskA est fermée pendant que la TaskB est en train de lire, la TaskB reste en mesure de lire les octets restants à lire, avant de fermer la connexion.
Read/Write n'ont pas besoin d'être synchronisées.


Broker :

Un Broker est créé directement dans le constructeur d'une tâche "Sender" dans lequel la connexion à un Channel est acceptée. Les tâches "Receiver" peuvent se connecter à un Channel existant en donnant le nom du Broker ayant initialisée la connexion (name) et le port sur lequel il a été créé. C'est une classe qui permet d'établir la connexion à plusieurs Channel pour une même tâche, et autoriser les communications entre les Task.

Accept : 

	Méthode permettant d'initialiser la connexion, à partir d'un port fourni. Cela permet de créer un 		
	nouveau Channel 	
	pour une connexion entre deux Tasks.
	Le Channel correspondant au port est retourné lorsque la connexion est acceptée.
	
Connect :

	Méthode permettant d'établir une connexion à un Channel déjà initialisée par le Broker courant. Il faut 
	que le nom soit le même que celui du Broker, le port également.
	Le booléen disconnected est initialisé à false (pour Channel) car la connexion est initialisée.