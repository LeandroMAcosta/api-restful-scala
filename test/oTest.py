#!/usr/bin/env python3
import json
import requests

error_n = 0
success_n = 0
HOST = "http://localhost:4000"

GREEN = '\033[92m'
RED = "\033[91m"
BOLD = "\033[1m"
END = "\033[0m"

def print_error(text):
    print(RED + BOLD + "[ERROR] " + END + text)

def print_success(text):
    print(GREEN + BOLD + "[SUCCESS] " + END + text)

def check_response(req, res):
    if(req["res_code"] != res.status_code):
        print_error(f"Bad code response \r\n\t Request: {req} \r\n\t Expected: {req['res_code']} \r\n\t Me diste wacho: {res.status_code}")
        return
    if(req["res_text"] != "" and req["res_text"] != res.text[1:-1]):
        print_error(f"Bad text response \r\n\t Request: {req} \r\n\t Expected: {req['res_text']} \r\n\t Me diste wacho: {res.text[1:-1]}")
        return
    print_success(f"{req['type']} {req['url']}")

def get_request(req):
    res = requests.get(HOST + req["url"], params=req["args"]) 
    check_response(req, res)

def post_request(req):
    res = requests.post(HOST + req["url"], json=req["args"])
    check_response(req, res)

def load_data():
    with open('test_data.json') as f:
        try:
            tests = json.load(f)
            return tests
        except Exception:
            print_error("Error en la lectura del json")

def main():
    reqs = load_data();
    if(reqs == None):
        return 0

    for r in reqs["list"]:
        if(r["type"] == "GET"):
            get_request(r)
        else:
            post_request(r)

    print(f"...Nos vimo...")
    

if __name__ == "__main__":
    main()
