import socket
from datetime import datetime

server = socket.socket()

server.setsockopt(socket.SOL_SOCKET,
                  socket.SO_REUSEADDR, 1)

server.bind(('localhost', 9999))
server.listen(5)

print("Server waiting for connections...")

while True:

    conn, addr = server.accept()

    print("Connected to:", addr)

    client_time = conn.recv(1024).decode()

    server_time = datetime.now()

    print("Client Time :", client_time)
    print("Server Time :", server_time)

    client_time_obj = datetime.strptime(
        client_time,
        "%Y-%m-%d %H:%M:%S"
    )

    avg_timestamp = (
        server_time.timestamp() +
        client_time_obj.timestamp()
    ) / 2

    sync_time = datetime.fromtimestamp(avg_timestamp)

    print("Synchronized Time :", sync_time)

    conn.send(str(sync_time).encode())

    conn.close()
