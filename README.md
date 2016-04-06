# Milestone3
<b>Marking Criteria</b><br>
The	final	assessment	requires	up	to	two	game	features	of	your	own	devising.	 Include	a	concise	
written	description	of	the	features,	explaining	the	key	coding	challenges	involved.

<b>Task</b><br>
Add two new features to the game

<b>My features</b><br>
<ul>
<li><b> Feature 1 </b><br> Main character moving animations <br>
This feature consist in giving a visual feedback of the player status like if it’s walking, jumping or shooting.
This has been implemented using a Timer and different status that make the images change behaving as animation.
<br><br>
<b>Challenging code: </b><br>
-	the hardest part it’s been finding a way to change shape and fixture of the actor in order to fit in the different images, but after many tries to erase the fixture list, as well as to change a single fixture in its position with a new one always with bad results I’ve decide to use a single boxshape that could match with all the images as best as possible. <br>
-	Another challenging part it’s been to effectively change the images according to the actual movements of the actor, and the moving system of the engine doesn’t help with it. This issues it’s been solved but with some weird behaviour while moving
</li>
<br>
<li> <b>Feature 2 </b><br> Final boss fine state machine <br>
This feature makes the final boss moving by itself not only in the X axis but also in the Y axis, based on the x position of the actor and on three states. The boss attacks the actor shooting at him every time that the character is in a certain range and after a few times that it goes in that range it does another attack (falling on it).
<br><br>
<b>Challenging code:</b><br>
-	The hard part it’s been using together two different timers one for the automatic movements and one needed for the falling attack in order to leave the body close to the ground for a while, giving the opportunity to the actor of hitting and then killing it. 
The solution has been found in the Javadoc in the getSource() method that return the object that has thrown the event.<br>
-	Another difficulty was to count how many times the player went in the range, because the timer loop was going too fast and so I couldn’t just increment a counter variable because it would have increased too quickly.
So I’ve made a few checks statements  to make sure that the counter was incremented only once for each time the player was inside the range.
</li>
</ul>

