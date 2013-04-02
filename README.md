n-body-problem
==============

A java physics simulator for simulating the force of gravity.

== Requirements ==
1. Java 1.7 (JRE)
2. Ant

== Building ==
The project was built in the eclipse IDE but can be compiled by ant if 
you wish. Either run the `make` command or the `ant` command and it will
compile the project with the correct.

== Running ==
The main programs that might be interesting to you as a tester are: 
 - 2D visualiser
 - the ResultGenerator. 

Available runnable scripts:
 - 2dvisualiser
 - grav_single
 - grav_multi
 - barneshut_single
 - barneshut_multi

== Input parameters ==

./2dvisualiser numBodies numSteps far numThreads collision width height barnes
./grav_single numBodies numSteps far numThreads collision
./grav_multi numBodies numSteps far numThreads collision
./barneshut_single numBodies numSteps far numThreads collision
./barneshut_multi numBodies numSteps far numThreads collision

numBodies and numSteps are mandatory, the rest is optional and will fallback to default values
if no others are entered.

Defaults:
	far			= 0.5
	numThreads 	= 1 or 2 	(Depending on if sequential run or multithreaded)
	collision	= true
	width 		= 640
	height 		= 480
	barnes 		= false


Definitions:
	
	far 		:= Granularity value in barnes-hut algorithm. Does nothing for other implementations. 
	
	numThreads 	:= The number of worker threads used. Does nothing for a sequential run.

	collision 	:= Set if collision physics should be activated or not.
	
	width		:= Width of the window for the GUI.
	
	height		:= Width of the window for the GUI.
	
	barnes		:= Set gravitational force to run with a Barnes-Hut implementation.