import socket
from datetime import datetime

client = socket.socket()

client.connect(('localhost', 9999))

current_time = datetime.now().strftime(
    "%Y-%m-%d %H:%M:%S"
)

client.send(current_time.encode())

print("Client Time Sent :", current_time)

sync_time = client.recv(1024).decode()

print("Synchronized Time Received :", sync_time)

client.close()
