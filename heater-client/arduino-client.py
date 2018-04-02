# This script should be run on a raspberry pi that's usb connected to an arduino.
# The serial port address might be different each time this script is run.
from serial import Serial

import urllib2
import time

# todo Enter endpoint for checking the flag in the database.
url = ''
ser = Serial('/dev/ttyACM2', 9600)

while True:
    contents = urllib2.urlopen(url).read()
    if str(contents).find("on") > -1:
        ser.write('1')
        print "Heating is On"
    if str(contents).find("off") > -1:
        ser.write('0')
        print "Heating is Off"
    time.sleep(5)
