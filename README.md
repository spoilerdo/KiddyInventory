# KiddyInventory
Inventory API is a subsystem of the Kiddy-enterprise software made for semester three software engineering at Fontys.
It communicates with an external authentication API called [KiddyBank](https://github.com/spoilerdo/KiddyBank).
The KiddyMarket project is an API that uses the KiddyInventory in order to keep track of the items obtained by a user.

#### Core aspects
The core aspects of this API is that it can store items that have been obtained by a user. 
It's also has transfer functionality so that 2 users can transfer items to each other.
The API is made with Gradle and Spring and uses Hibernate as ORM.
