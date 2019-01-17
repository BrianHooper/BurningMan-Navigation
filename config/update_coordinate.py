from random import seed, randint, uniform
import time

NORTH = 1
SOUTH = 2
WEST = 3
EAST = 4

def move(latitude, longitude):
	walk_step = 0.002

	direction = randint(1, 4)
	r = uniform(-walk_step, walk_step) * 0.1
	latitude_mod = latitude + r
	r = uniform(-walk_step, walk_step) * 0.1
	longitude_mod = longitude + r
	
	return latitude_mod, longitude_mod
latitude = 40.7844
longitude = -119.2045

for x in range(0, 100):
	latitude, longitude = move(latitude, longitude)
	with open("coordinate", 'w') as writer:
		writer.write("%.4f\n%.4f" % (latitude, longitude))
	time.sleep(1)
	