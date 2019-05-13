#!/usr/bin/env python3

"""Small script to automate the testing of the rapy server"""

import json
import requests

# Colors
YELLOW = '\033[93m'
GREEN = '\033[92m'
RED = "\033[91m"
BOLD = "\033[1m"
END = "\033[0m"

# Server data
HOST = "http://localhost:4000"

LOCATIONS = [
    {'name': 'FAMAF', 'coordX': 420, 'coordY': 666},
    {'name': 'Chascomus', 'coordX': 123, 'coordY': 456},
    {'name': 'El Sargento', 'coordX': 100, 'coordY': 100},
]

PROVIDERS = [
    {"username": "barquimicas", "location": "FAMAF",
     "storeName": "Bar de Ciencias Químicas", "maxDeliveryDistance": 314159},
]

CONSUMERS = [
    {"username": "estudiante_1", "location": "FAMAF"},
    {"username": "estudiante_2", "location": "FAMAF"},
]

ITEMS = [
    {"name": "Café", "description": "Calentito", "price": 25,
     "providerUsername": "barquimicas"},
]

ORDERS = [
    {
     "providerUsername": "barquimicas",
     "consumerUsername": "estudiante_1",
     "jsonItems": '[{"name": "Cafe", "amount": 3}]'
     }
]


def post_request(url, data, key, api_name):
    """Simple post request and status printing"""
    req = requests.post(url, json=data)

    if req.status_code != requests.codes["ok"]:
        print(RED + "ERROR" + END, "loading", api_name,
              data[key] + ":", YELLOW + req.text + END)
        return

    print("Created", api_name, data[key], "with id", req.text)

    req.close()


def get_request(url, api, arg=""):
    """Simple get request and status printing"""
    req = requests.get(url + arg)

    if req.status_code != requests.codes["ok"]:
        print(RED + "ERROR" + END + " getting %s: %s" % (api, req.text))
        return

    data = json.loads(req.text)
    print(GREEN + BOLD + api + ":" + END)

    for i in data:
        print(i)


def main():
    """Test all the GET and POST commands"""

    print(BOLD + "Trying to contact server..." + END)
    try:
        requests.get(HOST)
    except Exception as ex:
        print(RED + "ERROR" + END + " connecting to server %s: %s"
              % (HOST, ex))
        return

    print(BOLD + "Loading data into the server..." + END)

    # Load locations
    for loc in LOCATIONS:
        post_request(HOST + "/api/locations", loc, "name", "Location")

    # Load providers
    for prov in PROVIDERS:
        post_request(HOST + "/api/providers", prov, "username", "Provider")

    # Load consumers
    for cons in CONSUMERS:
        post_request(HOST + "/api/consumers", cons, "username", "Consumer")

    # Load items
    for item in ITEMS:
        post_request(HOST + "/api/items", item, "name", "Item")

    # Load orders
    # for order in ORDERS:
    #     post_request(HOST + "/api/orders", order, "providerUsername", "Order")

    # Get data
    print(BOLD + "Getting data..." + END)
    get_request(HOST + "/api/locations", "Locations")
    get_request(HOST + "/api/consumers", "Consumers")
    get_request(HOST + "/api/items", "Items")
    # get_request(HOST + "/api/orders", "Orders")
    for i, _ in enumerate(ORDERS):
        get_request(HOST + "/api/orders/detail", "Details", "?id=%d" % (i+1))

    for prov in PROVIDERS:
        get_request(HOST + "/api/providers", "Providers",
                    "/?location=" + prov["location"])


if __name__ == "__main__":
    main()
