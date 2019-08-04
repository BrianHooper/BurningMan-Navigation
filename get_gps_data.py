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
    raw_data = execute_command("gpspipe -w | head -n 10 | tail -n 1")
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
                    return None

            return str(round(lat, 4)), str(round(lon, 4))

        else:
            print("Error: Keys not in string " + raw_data, file=sys.stderr)
            return None
    except json.JSONDecodeError:
        print("Error decoding string " + raw_data, file=sys.stderr)
        return None


while True:
    sleep(3)
    print("Getting coordinates")
    gps = get_coord()
    if gps is not None:
        lat, lon = gps
#       with open("config/coordinate", "w") as outfile:
#           outfile.write(lat + "\n")
#           outfile.write(lon + "\n")
        print("Recieved %s, %s" % (lat, lon))
