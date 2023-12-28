# Multi-Threaded Chat Application
## Overview
This project aims to create a multi-threaded application enabling multiple users to communicate via a chat interface concurrently. The core focus lies in the utilization of multi-threading, facilitating the execution of various tasks simultaneously. In this scenario, multi-threading is employed to manage sockets for different users connecting to the chat. The principle of sockets, serving as Java objects facilitating communication between a client and a server, is highlighted. Additionally, the project leverages a hashmap, a data structure utilized to store information about each socket created by a thread. This mechanism ensures that messages sent by clients are broadcasted to all connected users.

## Features
- Multi-Threading: Enables concurrent execution for managing user sockets.
- Socket Communication: Utilizes Java sockets for client-server communication within the chat.
- Hashmap Implementation: Stores socket-related information to facilitate message broadcasting among connected users.
## Usage
1. Setup Environment:
- Ensure Java is installed on your system.
2. Compile and Run:
- Compile the Java files and run the main application file to initiate the multi-threaded chat server.
3. Connect to the Chat:
- Use a compatible chat client to connect to the server and start communicating with other users.
## Technologies Used
- Java
- Multi-Threading
- Sockets
- Hashmap Data Structure
