from threading import Thread
from time import sleep
from serial import Serial

import requests
import urllib2
import time

# todo Enter url + endpoint to POST temperature data.
url = ''
ser = Serial('/dev/ttyACM0', 9600)

while True:
	temperatureC = str(ser.readline()).strip()
	r = requests.post(url+temperatureC)
	print temperatureC
        time.sleep(5)

