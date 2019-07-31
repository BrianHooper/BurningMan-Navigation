from pathlib import Path

import datetime

events_path = Path("config/events.tsv")

with open(events_path, "r", encoding="utf-8") as infile:
    data = infile.readlines()

def to_epoch(month, day, hour):
    date_str = month + " " + day + " 2019 " + hour
    try:
        if ":" in hour:
            return datetime.datetime.strptime(date_str, '%B %d %Y %I:%M%p')
        else:
            return datetime.datetime.strptime(date_str, '%B %d %Y %I%p')
    except ValueError:
        print(month)
        print(day)
        print(hour)


def parse_single_time_string(full_string):
    full_string = full_string.replace("[", "")
    full_string = full_string.replace("]", "")
    full_string = full_string[1:-1]
    split = full_string.split(",")
    second_split = split[1].split(" ")

    month = second_split[-2]
    day = second_split[-1]

    time_split = split[-1].split(" – ")

    start_time = time_split[0].replace(" ", "")
    end_time = time_split[1].replace(" ", "")

    return to_epoch(month, day[:-2], start_time), to_epoch(month, day[:-2], end_time)

def parse_full_time_string(full_string):
    split = full_string.split("<-->")
    times = []
    for partial in split:
        times.append(parse_single_time_string(partial))
    return times



for line in data:
    split = line.split("\t")
    name = split[0]
    location = split[1]
    type = split[2]
    full_time_string = split[3]
    description = split[4]

    times = parse_full_time_string(full_time_string)
    print()
