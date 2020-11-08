# JusTalk
## Componenta echipei:
- Bizu Andreea-Felicia
- Tunaru Gabriel-Stefan
- Galuska Vlad-Cristian
- Nicolescu Alexandru-Leonard
## Detalii despre instalarea si rularea aplicatiei
- Rulare Server.java 
- Rulare Client.java cu argumentele: `nume localhost 8080` pentru fiecare client nou
## Arhitectura sistemului:
- Arhitectura e de tip client-server, comunicarea facandu-se prin Sockets
## Descrierea generala:
Un server care poate fi folosit de aplicatii pentru a comunica intre
  ele prin mesaje. Prin intermediul serverului pot fi efectuate trei
  tipuri de activitati:
  - trimitere de mesaje
  - receptie de mesaje
  - administrare
  
  Mesajele sunt trimise si receptionate prin intermediul a doua tipuri
  de resurse oferite de server:
  - message queues
  - topics
  
  Cozile de mesaje pot tine un numar maxim de mesaje la un moment dat,
  iar accesul lor se face dupa princpiul FIFO. Fiecare mesaj specifica
  intr-un antet destinatarul mesajului. Daca un program nu este
  destinatar al mesajului nu il va prelua. Preluarea unui mesaj implica
  scoaterea lui din coada.
  	
  Resursele de tip Topic permit publicarea de mesaje care pot fi citite
  de un numar nelimitat de clienti. Nu exista destinatari explicit
  specificati, dar fiecare mesaj are un anumit tip, memorat sub forma
  unui camp in antetul sau. Clientii care citesc mesaje specifica tipul
  mesajului pe care il doresc. La citire, mesajele nu sunt sterse, dar
  ele vor fi sterse automat dupa ce trece un timp de la
  publicarea lor, specificat in antetul mesajului. Serverul poate fi
  configurat cu un timp maxim de valabilitate a mesajelor, masurat de la
  momentul publicarii. Dupa trecerea acestui
  timp, mesajele vor fi sterse indiferent de timpul specificat in
  antetul lor.
## Detalii despre implementare
Clientul si serverul comunica prin `Sockets`.In partea de client afisam un meniu care ofera 5 optiuni. Pentru fiecare client cand se stabilieste conexiunea se porneste in Server un `thread` care se ocupa de el. Numele fiecarui client impreuna cu socket id-ul asociat sunt salvate intr-o `ConcurrentSkipListMap`.<br>
Coada de mesaje foloseste `ConcurrentLinkedQueue` pentru a salva mesajele intr-un mod thread safe, si este de tipul `Singleton`. In cazul in care mesajul din varf nu e adresat clientului care il acceseaza, acesta nu il preia(nu se blocheaza, poate face orice altceva pana cand va fi posibil sa preia). Daca destinatarul mesajului din varful stivei de mesaje, nu e conectat, mesajul este sters. <br>
In Topic(care e `Singleton`) ne folosim de `ConcurrentSkipListMap` pentru a separa mesajele din Topic-uri in functie de tipul lor, iar fiecare mesaj din Topic e salvat intr-o `ConcurrentSkipListSet`. Folosim un `TimerTask` care o data la 5s verfica daca au expirat mesajele din Topic si le sterge, daca da. Serverul e configurat cu timpul de expirare a mesajelor din Topic de 1 minut.
## Probleme de concurenta
Ne-am folosit de mai multe colectii concurente pentru a evita posibilele erori de scriere/accesare concurenta a mesajelor(`ConcurrentLinkedQueue`), a topicurilor(`ConcurrentSkipListMap`, `ConcurrentSkipListSet`) si a clientilor(`ConcurrentSkipListMap`). De asemenea am eliminat situatia in care clientii s-ar fi blocat in asteparea preluarii mesajului din varful stivei de catre un client inexistent(cand un client incerca sa acceseze mesajul din varf, verificam daca destinatarul exista, daca nu eliminam mesajul).
