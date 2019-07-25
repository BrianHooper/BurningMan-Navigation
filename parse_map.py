import xml.etree.ElementTree as ET
import operator

delimiter = "\t"

address_mapping = {
    "Andromeda": "A",
    "Bacchus": "B",
    "Cupid": "C",
    "Diana": "D",
    "Echo": "E",
    "Fortuna": "F",
    "Ganymede": "G",
    "Hyacinth": "H",
    "Icarus": "I",
    "Jove": "J",
    "Kronos": "K",
    "Labyrinth": "L",
}

def replace_address(address):
    if address is None:
        return address
    for key in address_mapping:
        address = address.replace(key, address_mapping[key])
    return address

class Location:
    def __init__(self, name, address, description):
        self.name = name
        self.address = replace_address(address)
        self.description = description

    def __str__(self):
        if self.description is None:
            return str(self.name) + delimiter + str(self.address)
        else:
            return str(self.name) + delimiter + str(self.address) + delimiter + str(self.description)


def parse_theme_camps(theme_camps):
    parsed_theme_camps = []
    for camp in theme_camps[1:]:
        extended_data = camp.find('{http://www.opengis.net/kml/2.2}ExtendedData')
        name = camp[0].text.replace("\n", "")
        address = extended_data[3][0].text.replace("\n", "")
        description = extended_data[4][0].text
        if description is not None:
            description = description.replace("\n", "")
        parsed_theme_camps.append(Location(name, address, description))
    return sorted(parsed_theme_camps, key=operator.attrgetter('name'))


def parse_facilities(brc_facilities):
    parsed_facilities = []
    for facility in brc_facilities[1:]:
        extended_data = facility.find('{http://www.opengis.net/kml/2.2}ExtendedData')
        name = facility[0].text.replace("\n", "")
        address = extended_data[1][0].text
        if address is not None:
            address = address.replace("\n", "")
        description = extended_data[3][0].text
        if description is not None:
            description = description.replace("\n", "")
        parsed_facilities.append(Location(name, address, description))
    return sorted(parsed_facilities, key=operator.attrgetter('name'))


def valid_tag(child):
    return len(child.attrib) == 0 and child.tag == "{http://www.opengis.net/kml/2.2}Folder"


with open("C:\\Users\\v-brhoop\\Downloads\\2019 Unofficial Map of Black Rock City for Burning Man.kml", "r", encoding="utf-8") as infile:
    tree = ET.parse(infile).getroot()[0]

layers = list(filter(valid_tag, tree))
theme_camps = layers[0]
brc_facilities = layers[2]

parsed_theme_camps = parse_theme_camps(theme_camps)
parsed_facilities = parse_facilities(brc_facilities)

with open("C:\\Users\\v-brhoop\\Documents\\theme_camps.tsv", 'w', encoding="utf-8") as outfile:
    for camp in parsed_theme_camps:
            outfile.write("%s\n" % str(camp))
with open("C:\\Users\\v-brhoop\\Documents\\facilities.tsv", 'w', encoding="utf-8") as outfile:
    for facility in parsed_facilities:
            outfile.write("%s\n" % str(facility))