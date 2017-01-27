#DESIGN
Goals: The goal of this project was to create
       a fully functioning variant of breakout 
       with features that allowed it to be 
       interesting and fun to play. I think
       I achieved this goal. Although there are bugs,
       the features I added made for very interesting 
       gameplay. 
       Depending on what kind of feature
       you're trying to add, you would have to go 
       into the code and add another if statement
       for that condition. Like for a new key input, 
       you would go into the appropriate method and
       add a statement for if the keyCode 
       that you want to implement is pressed.
       For brick type, you would need to go into
       the brick class and code the behavior for
       the type of brick, and create an if statement 
       for the collision with that brick to see
       what happens. 
       Some major design decisions that I made were
       to make a collision detector that follows
       around the image views to check for 
       collisions. The reason I did it this way is 
       because it was easier to do it with the 
       boiler-plate code given to me. If I had more
       time, I would have researched more APIs 
       and found a more appropriate way. A big
       con with this was that it did not function
       the way I wanted, but it saved me enough time
       to make other aspects of my project better.
       Another major design decision for the sake
       of time was to put all my code into one class.
       It was sloppy and uncompartmentalized, but
       it saved enough time for me to actually finish
       the project.
       Another thing I decided to do was to end the
       game immediately when the third level is beat
       or when the ball leaves the screen. I was
       trying to make a message pop up to make it 
       more like a real game, but this was taking too
       much time, and I wanted to submit on time. 
       