#Project 5 : Graphical Adventure Game

##1. About/Overview

Given the dungeon, the problem is to make the player in the dungeon move, shoot or pick up the treasure or arrow in the cave using inputs given by the user. The caves in the dungeon will have monsters which can be killed when the player shoots and if the player enters the cave with monsters, the player might get killed depending on the health of the player. The dungeon also has a moving monster, with which when encountered, the player has to fight a combat. There's a thief which can steal the treasure from the player and a pit in which the player can fall and die. The player has to fight all the obstacles and reach end.

##2. List of Features

* Text based commands for the user to select the next move.
* GUI for the user to play withh visuals.
* The player can shoot in a certain direction with a particular distance entered.
* The player can pick up arrows or treasures in the cave.
* There are monsters in the cave which can either kill the player if he enters the cave or can get killed if the player shoots at that cave.
* The moving monster and the player have to fight the combat.
* The thief can steal player's treasures.
* There is a pit in the dungeon in which player can die.

##3.How To Run

1. Open terminal
2. Navigate to the location where you have stored the jar file.
3. You need to provide the below mentioned parameters as command line arguments in the order:
    1. number of rows
    2. number of columns
    3. The degree of interconnectivity
    4. Boolean value to show if the  dungeon is wrapped or non-wrapped-
        * Wrapping Dungeon : true
        * Non-wrapping Dungeon : false
    5. The percentage of caves which should contain treasure
    6. The number of monsters in the dungeon.
4. Run the jar file using the
   command java - jar DungeonController.jar <numOfRows> <numOfColumns> <interconnectivity> <percentOfCavesToConatinTreasure> <isDungeonWrapping>

##4. How To Use The Program

* The program will provide a description of the dungeon and the player initially.
* The program will the ask the user for the next move, if the user wants the player to move in a certain direction, shoot arrow or pick up treasure or arrow.
* If the user enters M(move), the program will ask for the direction in which the player has to move.
* If the user enters S(shoot), the program will ask for the distance and the direction in which the arrow has to be shot.
* If the user enters P(pick up), the program will ask what does the player want to pick up, arrow or treasure.
* The game will continue until the player has reached the final destination, or till the player has been killed and eaten by the monster.


##5. Description of Examples

Run1 – Run_Reached_Destination.txt

1. The user has to give the rows, columns, wrapping as false for testing wrapped dungeon, interconnectivity, treasure percent and number of monsters in the dungeon.
2. The dungeon is then displayed in a detailed manner and the start and end caves are displayed along with the player description.
3. The user is then given an option to move, pick up and shoot.
4. The user shoots the monsters and in the end gets eaten by the monster.

Run2 – Run_Pick_Shoot_Eaten.txt
1. The user has to give the rows, columns, wrapping as false for testing wrapped dungeon, interconnectivity, treasure percent and number of monsters in the dungeon.
2. The dungeon is then displayed in a detailed manner and the start and end caves are displayed along with the player description.
3. The user is then given an option to move, pick up and shoot.
4. The user picks up the arrow, picks the treasures, shoots the monsters and then moves along to reach the destination.



##6.Design/Model Changes

The following changes have been made since the previous design meeting:

* Created a command controller instead of a simple controller in order to simplify the usages of the commands.
* Created a separate class for moving monster in order to keep both the creatures separate.


##7. Assumptions

We are assuming the following:

* Each cave can have upto three treasures of any type.
* Each cave can have upto three arrows.

##8. Limitations

* The program allows the player to pick to the all the treasures present in the cave and not partially.
* The program allows the player to pick to the all the arrows present in the cave and not partially.

##9. Citations

* Breadth First Search - https://www.geeksforgeeks.org/breadth-first-search-or-bfs-for-a-graph/
* Kruskal's algorithm - https://www.baeldung.com/java-spanning-trees-kruskal
* Kruskal's algorithm - https://www.techiedelight.com/kruskals-algorithm-for-finding-minimum-spanning-tree/
* Controller - https://northeastern.instructure.com/courses/90366/pages/module-8-mvc-controllers?module_item_id=6535605

