import subprocess
import json
import sys
from pathlib import Path

# Location of coordinate file must match location in View/Location class
from time import sleep

coord_path = Path("/home/brian/Documents/code/java/BurningMan-Navigation/config/coordinate")


def execute_command(command_string):
    return subprocess.run(command_string, stdout=subprocess.PIPE, stderr=subprocess.PIPE, shell=True).stdout.decode(
        "utf-8")


def get_coord():
    raw_data = execute_command("gpspipe -w | head -n 5 | tail -n 1")
    try:
        json_data = json.loads(raw_data)
        if "lat" in json_data and "lon" in json_data:
            lat = json_data["lat"]
            lon = json_data["lon"]

            if type(lat) != float or type(lon) != float:
                try:
                    lat = float(lat)
                    lon = float(lon)
                except ValueError:
                    print("Error converting values to float: " + str(lat) + ", " + str(lon), file=sys.stderr)
                    exit(-1)

            return str(round(lat, 4)), str(round(lon, 4))

        else:
            print("Error: Keys not in string " + raw_data, file=sys.stderr)
            exit(-1)
    except json.JSONDecodeError:
        print("Error decoding string " + raw_data, file=sys.stderr)
        exit(-1)


while True:
    sleep(10)
    print("Getting coordinates")
    lat, lon = get_coord()
    with open("config/coordinate", "w") as outfile:
        outfile.write(lat + "\n")
        outfile.write(lon + "\n")
    print("Recieved %s, %s" % (lat, lon))
