# Python3 program imitating a Clock Server

from dateutil import parser
import threading
import datetime
import socket
import time


# Dictionary to store client data
client_data = {}


# Function to receive clock time from clients
def startReceivingClockTime(connector, address):

    while True:
        try:
            # Receive client clock time
            clock_time_string = connector.recv(1024).decode()

            # Check if client disconnected
            if not clock_time_string:
                print("Client disconnected:", address)
                del client_data[address]
                break

            # Convert string into datetime object
            clock_time = parser.parse(clock_time_string)

            # Calculate time difference
            clock_time_diff = datetime.datetime.now() - clock_time

            # Store client data
            client_data[address] = {
                "clock_time": clock_time,
                "time_difference": clock_time_diff,
                "connector": connector
            }

            print("Client", address, "sent time:", clock_time)

            time.sleep(5)

        except Exception as e:
            print("Error receiving time from", address, ":", e)

            if address in client_data:
                del client_data[address]

            break


# Function to accept client connections
def startConnecting(master_server):

    while True:

        # Accept client connection
        master_slave_connector, addr = master_server.accept()

        slave_address = str(addr[0]) + ":" + str(addr[1])

        print(slave_address, "got connected successfully")

        # Create thread for each client
        current_thread = threading.Thread(
            target=startReceivingClockTime,
            args=(master_slave_connector, slave_address)
        )

        current_thread.start()


# Function to calculate average clock difference
def getAverageClockDiff():

    time_difference_list = [
        client['time_difference']
        for client_addr, client in client_data.items()
    ]

    sum_of_clock_difference = sum(
        time_difference_list,
        datetime.timedelta(0, 0)
    )

    average_clock_difference = (
        sum_of_clock_difference / len(client_data)
    )

    return average_clock_difference


# Function to synchronize all client clocks
def synchronizeAllClocks():

    while True:

        print("\n-----------------------------------")
        print("New synchronization cycle started")
        print("Number of clients:",
              len(client_data))

        if len(client_data) > 0:

            # Calculate average clock difference
            average_clock_difference = getAverageClockDiff()

            # Calculate synchronized time
            synchronized_time = (
                datetime.datetime.now() +
                average_clock_difference
            )

            print("Synchronized Time:",
                  synchronized_time)

            # Send synchronized time to all clients
            for client_addr, client in client_data.items():

                try:
                    client['connector'].send(
                        str(synchronized_time).encode()
                    )

                    print("Synchronized time sent to:",
                          client_addr)

                except Exception as e:

                    print("Error sending time to",
                          client_addr, ":", e)

        else:
            print("No client data. Synchronization not possible.")

        print("-----------------------------------\n")

        time.sleep(5)


# Function to start clock server
def initiateClockServer(port=8080):

    master_server = socket.socket()

    master_server.setsockopt(
        socket.SOL_SOCKET,
        socket.SO_REUSEADDR,
        1
    )

    print("Socket at master node created successfully\n")

    # Bind socket
    master_server.bind(('', port))

    # Listen for clients
    master_server.listen(10)

    print("Clock Server Started...\n")

    # Thread for accepting clients
    master_thread = threading.Thread(
        target=startConnecting,
        args=(master_server,)
    )

    master_thread.start()

    # Thread for synchronization
    sync_thread = threading.Thread(
        target=synchronizeAllClocks
    )

    sync_thread.start()


# Driver Code
if __name__ == '__main__':

    initiateClockServer(port=8080)
