
# In general
* Determine when a single game piece has come into contact with any of a list of game pieces
  Spawn/remove pieces accordingly
* Move a list of game pieces from their current position to their next positions
* Remove game pieces that have left the screen
* spawn ships randomly

All of these directly imply that your program also needs to do the following:
* Determine when a single game piece has come into contact with another game piece
* Produce new bullets when one explodes
* Move a single game piece from its current position to its next position
* Determine when a game has left the screen


# In each class
In AEntity:
  move one tick
  Abstract draw
  Abstract detect collision with another piece (double dispatch)
  return list of entities on explode

In ILoEntity:
  ILoEntity move all
  WorldScene draw all
  ILoEntity collide all

In MyWorld:
  In onTick:
    move entities
    collide entities
    spawn ships
  In make scene:
    draw all entities
  In onKey event:
    spawn new bullet if correct key pressed
  In worldEnds:
    world end if
      all bullets fired
      none left on screen
