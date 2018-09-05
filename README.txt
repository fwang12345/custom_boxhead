=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=
CIS 120 Game Project README
PennKey: frankiew
=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=:=

===================
=: Core Concepts :=
===================

- List the four core concepts, the features they implement, and why each feature
  is an appropriate use of the concept. Incorporate the feedback you got after
  submitting your proposal.

  1. Collection: I use lists and sets to store my game objects based on the situation. If i need to
  compares many pairs, I use lists because there is a get(int index) function and each object is
  the same reference time if I use ArrayList. In cases where I just need to store all objects, I use
  tree sets for faster removal. 

  2. Inheritance: All objects in my game including projectiles and characters are under the abstract class 
  of Unit. This interface extends JPanel so that every class that extends Unit can be drawn a specific way.
  Furthermore, my classes are further subdivided by Vulnerable which extends Unit. Vulnerable are basically
  Units that are able to be killed. This includes characters. I also have weapons such as Pistol, Uzi, and 
  Shotgun that extend abstract class Weapon because every weapon fires differently. For instance, shotguns
  fire many rounds at once compared to pistols or uzis.

  3. Recursive Data Structure: I created a data structure called QuadTrees that are recursive in that they
  each contain 4 child QuadTrees. These QuadTrees represent the space of my game and allows me to divide
  my space into smaller components. This way, objects in one component of my space only have to be compared to
  objects in the same component. This is to lower the amount of comparisons when detecting collisions as there 
  can be many units at once.

  4. Recursive Algorithms. I use recursive algorithms to detect collisions and to insert Units to the 
  QuadTree. These algorithms are necessary because we do not know the depth of the QuadTree and only know
  to stop when we reach the base case where the QuadTree has no children(The 4 children are null).

=========================
=: Your Implementation :=
=========================

- Provide an overview of each of the classes in your code, and what their
  function is in the overall game.
  Boxhead: Represents the player's character and includes fields such as all the weapons and health
  Command: Functions as a guide for how to upgrade weapons when a certain multiplier is reached
  Devil: A class that extends JPanel and is a Devil enemy that has a notable Timer that 
  tracks when to shoot a projectile
  Direction: Enum that represents the four directions a Unit can have
  Game: Class where the game runs
  GameScreen: Where the game is played. This class has fields that tracks the state of the game
  such as score or round.
  LineSeg: This represents the trajectory of a bullet and has intersect functions that determines if
  the bullet hit anything.
  Message: This class extends JPanel and contains all the temporary messages that may pop up in 
  game such as any upgrades or ammo refills or health ups.
  PauseScreen: This class extends JPanel and contains a table of all weapon upgrades currently 
  achieved
  Pistol: A class that extends weapons characterized by short range and low fire rate
  Projectile: A class that extends Unit and represents projectiles shot by the Devil. These projectiles
  are not checked for collision in QuadTree because they only be need to be compared to the Boxhead
  QuadTree: A recursive data structure that checks sfor collisions between Vulnerable objects.
  Resource: A class that extends JPanel and represents the orange blocks that has a generate function
  to either increase ammo or health/
  Shotgun: A class that extends weapons characterized by many bullets and high damage
  Unit: An abstract class that extends JPanel and represents all objects in the game
  UpgradeType: An enum that represents all possible types of upgrades such as ammo upgrade or range
  upgrade.
  Uzi: A class that extends weapons characterized by long range and high fire rate.
  Vulnerable: A class that extends Unit and represents all killable objects.
  Weapon: An abstract class that represents all weapons in the game
  Zombie: A class that extends JPanel and deals damage to Boxhead when it interects with the Boxhead

- Were there any significant stumbling blocks while you were implementing your
  game (related to your design, or otherwise)?
  Mainly just deciding if code should be separated in a new class or whether they should remain in the 
  class.

- Evaluate your design. Is there a good separation of functionality? How well is
  private state encapsulated? What would you refactor, if given the chance?
  I believe I did a good job of preventing game breaking changes in elements such as weapons or zombies.
  If I had the chance, I would introduce more static final variables so that I know what fields
  vary every game and which remain the same.



========================
=: External Resources :=
========================

- Cite any external resources (libraries, images, tutorials, etc.) that you may
  have used while implementing your game.
  I only used swing, awt, and collections library.
