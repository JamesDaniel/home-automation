from serial import Serial

import urllib2
import time

url = 'http://localhost:8080/livingRoom/heartbeatOilFilled1'

while True:
    contents = urllib2.urlopen(url, data="").read()
    print "beat"
    time.sleep(5)


