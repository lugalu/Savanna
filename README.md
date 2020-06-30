#Savanna

This project was made for the concurrent programming class, 
its purpose is to simulate a warehouse suppliers using   threads and the client-server model.

##Specifications

design specifications

* Register products;
* Remove the product;
* List products
* Chat;

User

* Username:
* User ID (exclusive); (should be random, but Savanna chose to sequential)
* ArrayList of registered products. 

Chat
It must be initialized whenever the user wants. Connects the user to a server focused on chat operations.
The Chat username must match the user's name.
all clients should be able to log in to the chat, so threads are essential.

Known bugs:
After opening the chat if the user closes it, it cannot reopen without freezing the program
