from pathlib import Path
import os
import requests
from bs4 import BeautifulSoup

delimiter = "\t"

url_patterns = {
    "sun.htm": (28, 86),
    "mon.htm": (28, 86),
    "tues.htm": (28, 52),
    "wed.htm": (28, 52),
    "thurs.htm": (28, 52),
    "fri.htm": (28, 52),
    "sat.htm": (28, 52),
    "sun_2.htm": (28, 52),
    "mon_2.htm": (28, 52),
}

def progress_bar(percent):
    width = 62
    num_pounds = int(percent * width)
    num_dashes = width - num_pounds

    pounds = "#" * num_pounds
    dashes = "-" * num_dashes
    print("\rProgress: %06.2f%% %s%s" % (percent * 100, pounds, dashes), end="")
    if percent == 1:
        print()


class PlayaEvent():
    def __init__(self, name, address, event_type, dates, description):
        self.name = name
        self.address = address
        self.event_type = event_type
        self.dates = dates
        self.description = description

    def __str__(self):
        return str(self.name) + delimiter + str(self.address) + delimiter + str(self.event_type) + delimiter + str(self.dates) + delimiter + str(self.description)


def extract_urls(filename, section):
    with open(filename, "r", encoding="utf-8") as infile:
        lines = infile.readlines()
    urls = []
    for line in lines:
        if len(line) >= section[1]:
            if section[1] == 52:
                urls.append("https://playaevents.burningman.org" + line[section[0]:section[1]])
            else:
                urls.append(line[section[0]:section[1]])
    return urls


def get_child(body, index):
    if body is None:
        return None
    elif index > len(list(body.children)):
        return None
    else:
        return list(body.children)[index]


def remove_break(string):
    match = "<br/>"
    t = str(string) != match
    return t

working_directory = Path("C:\\Users\\v-brhoop\\Downloads\\Events")
os.chdir(working_directory)

urls = []
for key in url_patterns:
    urls += extract_urls(key, url_patterns[key])

parsed_events = []
num_events = len(urls)
for index, url in enumerate(urls):
    progress_bar(index / num_events)
    page = requests.get(url)
    soup = BeautifulSoup(page.content, 'html.parser')

    html = get_child(soup, 1)
    html = get_child(html, 1)
    html = get_child(html, 2)
    html = get_child(html, 1)
    html = get_child(html, 0)
    html = get_child(html, 0)
    html = get_child(html, 1)
    html = get_child(html, 0)

    if html is not None:
        event_name = list(html.children)[1].get_text()
        dates = list(filter(remove_break, list(list(list(html.children)[2])[1].children)))
        event_type = list(list(html.children)[3].children)[1].get_text()
        camp_name = list(list(html.children)[4].children)[1].get_text()
        description = list(list(html.children)[5].children)[1].get_text()
        playa_event = PlayaEvent(event_name, camp_name, event_type, dates, description)
        parsed_events.append(playa_event)

progress_bar(1)
with open("events.tsv", "w", encoding="utf-8") as outfile:
    for event in parsed_events:
            outfile.write("%s\n" % str(event))