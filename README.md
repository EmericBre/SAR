La branche utilisée pour l'ensemble de ce projet a été la branche **Emeric-Breton**


Le travail est séparée en 3 versions :

- La v1 qui regroupe le travail des premières semaines (Channel + Buffer + Broker). Elle ne contient que deux tests : Main dans l'implémentation et Test10Threads dans la partie Test.
Les tests fonctionnent mais les classes ont été largement modifiées dans les versions suivantes.

- La v2 qui reprend le travail de la v1 en ajoutant MessageQueue et QueueBroker, mais également le Broadcast. Les tests montrent que l'implémentation est fonctionnelle, cependant dans les versions "SansArret" il peut y avoir de l'interblocage au bout d'un certain temps.

- La v3 qui reprend le travail de la v2 en modifiant l'implémentation pour essayer d'avoir des events. Les events ont été designés pour ne pas faire le broadcast (pas eu le temps de réussir à implémenter les events pour le broadcast). Les tests montrent qu'il y a des problèmes et qu'il y a de l'interblocage fréquemment.
Ma compréhension des events et de leur utilisation n'est pas suffisamment bonne pour aller plus loin. Sans explications de la part d'autres étudiants ayant mieux compris le principe, je n'aurai pas réussi à faire une grosse partie d'event.

Tests :

v1 : Dans Implémentation, lancer Main ou dans Test, lancer Test10Threads.

v2 : Dans Broadcast, il y a une version avec un seul tour de boucle (AvecArret) et une version infinie (SansArret), lancer BroadcastTest dans chaque.
Dans Test, il y a aussi une version avec un seul tour de boucle et une version infinie. Dans la version AvecArret, il y a les tests Main, Test10Threads et Test1000Threads. Dans la version SansArret, il n'y a que Test1000Threads.

v3 : Dans Event.Test, lancer Main (le test se bloquera rapidement)


