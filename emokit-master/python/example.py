# This is an example of popping a packet from the Emotiv class's packet queue
# and printing the gyro x and y values to the console. 

from emokit.emotiv import Emotiv
import gevent

if __name__ == "__main__":
    headset = Emotiv()
    gevent.spawn(headset.setup)
    gevent.sleep(0)
    try:
        while True:
            packet = headset.dequeue()
            print str(packet.sensors['F3']['value']) + " " + \
                  str(packet.sensors['F4']['value']) + " " + \
                  str(packet.sensors['P7']['value']) + " " + \
                  str(packet.sensors['FC6']['value']) + " " + \
                  str(packet.sensors['F7']['value']) + " " + \
                  str(packet.sensors['F8']['value']) + " " + \
                  str(packet.sensors['T7']['value']) + " " + \
                  str(packet.sensors['P8']['value']) + " " + \
                  str(packet.sensors['FC5']['value']) + " " + \
                  str(packet.sensors['AF4']['value']) + " " + \
                  str(packet.sensors['T8']['value']) + " " + \
                  str(packet.sensors['O2']['value']) + " " + \
                  str(packet.sensors['O1']['value']) + " " + \
                  str(packet.sensors['AF3']['value'])

            #gevent.sleep(0) commented out to improve performance
    except KeyboardInterrupt:
        headset.close()
    finally:
        headset.close()
