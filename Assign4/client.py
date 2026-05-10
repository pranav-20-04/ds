# Python3 program imitating a client process

from dateutil import parser
import threading
import datetime
import socket
import time


# Function to send client clock time to server
def startSendingTime(slave_client):

    while True:
        try:
            # Get current client time
            current_time = datetime.datetime.now()

            # Send current time to server
            slave_client.send(str(current_time).encode())

            print("Client sent time:", current_time)

            time.sleep(5)

        except Exception as e:
            print("Error while sending time:", e)
            break


# Function to receive synchronized time from server
def startReceivingTime(slave_client):

    while True:
        try:
            # Receive synchronized time
            data = slave_client.recv(1024).decode()

            # Check if server disconnected
            if not data:
                print("Server disconnected")
                break

            synchronized_time = parser.parse(data)

            print("Synchronized time received from server:",
                  synchronized_time)

        except Exception as e:
            print("Error while receiving synchronized time:", e)
            break


# Function to start client
def initiateSlaveClient(port=8080):

    slave_client = socket.socket()

    # Connect to server
    slave_client.connect(('127.0.0.1', port))

    print("Connected to Clock Server\n")

    # Thread for sending client time
    send_time_thread = threading.Thread(
        target=startSendingTime,
        args=(slave_client,)
    )

    send_time_thread.start()

    # Thread for receiving synchronized time
    receive_time_thread = threading.Thread(
        target=startReceivingTime,
        args=(slave_client,)
    )

    receive_time_thread.start()


# Driver Code
if __name__ == '__main__':

    initiateSlaveClient(port=8080)
