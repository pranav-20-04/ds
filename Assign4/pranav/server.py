from functools import reduce
from dateutil import parser
import threading
import socket
import time
import datetime

# Global data structure
client_data = {}
data_lock = threading.Lock() # <-- ADD LOCK FOR THREAD SAFETY

def startReceivingClockTime(connector, address):
    while True:
        try:
            clock_time_string = connector.recv(1024).decode()
            if not clock_time_string:
                break # Client disconnected
            clock_time = parser.parse(clock_time_string)
            clock_time_diff = datetime.datetime.now() - clock_time
            
            with data_lock: # <-- LOCK WHEN WRITING
                client_data[address] = {
                    "clock_time": clock_time,
                    "time_difference": clock_time_diff,
                    "connector": connector
                }
            print(f"Client Data updated with: {address}")
            time.sleep(5)
        except Exception as e:
            print(f"Error receiving from {address}: {e}")
            break

def startConnecting(master_server):
    while True:
        master_slave_connector, addr = master_server.accept()
        slave_address = str(addr[0]) + ":" + str(addr[1])
        print(f"{slave_address} got connected successfully")
        
        current_thread = threading.Thread(
            target=startReceivingClockTime,
            args=(master_slave_connector, slave_address)
        )
        current_thread.daemon = True # Allow thread to exit when main exits
        current_thread.start()

def getAverageClockDiff():
    with data_lock: # <-- LOCK WHEN READING
        if not client_data:
            return None
        # Use a snapshot to avoid modification during iteration
        snapshot = client_data.copy()
        
    time_difference_list = [
        client['time_difference'] for client in snapshot.values()
    ]
    
    if not time_difference_list:
        return None
        
    sum_of_clock_difference = reduce(
        lambda x, y: x + y, time_difference_list, datetime.timedelta(0, 0)
    )
    average_clock_difference = sum_of_clock_difference / len(snapshot)
    return average_clock_difference

def synchronizeAllClocks():
    while True:
        print("\n--- New synchronization cycle started. ---")
        
        avg_diff = getAverageClockDiff()
        
        if avg_diff is not None:
            with data_lock:
                # Create a snapshot of connectors to send to
                snapshot = client_data.copy()
            
            print(f"Number of clients to be synchronized: {len(snapshot)}")
            
            for client_addr, client in snapshot.items():
                try:
                    synchronized_time = datetime.datetime.now() + avg_diff
                    time_str = str(synchronized_time)
                    print(f"Sending to {client_addr}: {time_str}")
                    client['connector'].send(time_str.encode())
                except Exception as e:
                    print(f"Failed to send to {client_addr}: {e}")
        else:
            print("No client data. Synchronization not applicable.")
            
        time.sleep(5)

def initiateClockServer(port=8099):
    master_server = socket.socket()
    master_server.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
    print("Socket at master node created successfully\n")
    
    master_server.bind(('', port))
    master_server.listen(10)
    print(f"Clock server started on port {port}...\n")
    
    print("Starting to make connections...\n")
    master_thread = threading.Thread(target=startConnecting, args=(master_server,))
    master_thread.daemon = True
    master_thread.start()
    
    print("Starting synchronization parallelly...\n")
    sync_thread = threading.Thread(target=synchronizeAllClocks)
    sync_thread.daemon = True
    sync_thread.start()
    
    # Keep main thread alive
    try:
        while True:
            time.sleep(1)
    except KeyboardInterrupt:
        print("Server shutting down.")

if __name__ == '__main__':
    initiateClockServer(port=8099)