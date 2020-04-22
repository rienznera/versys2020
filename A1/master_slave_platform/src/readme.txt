Ausführung wie folgt

RUN: Server <port> <max_slaves> <timeout>
RUN: Client <client_id> <port> <respond>  // this can be done multiple times BUT USE DIFFERENT IDS!!


Example Setup:

Server 9009 2 10000
Slave 1 9009 true
Slave 2 9009 false