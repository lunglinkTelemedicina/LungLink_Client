## LungLink_Client IS DESIGNED FOR: 
- Registering and Logging as patients
- Entering symptoms
- Recording signals (ECG/EMG) via BITalino and sending them to the server
- Viewing medicalHistory
- Communicating with the server using a TCP command-based protocol

## PROJECT STRUCTURE
```
libs/
├── bluecove-2.1.1.jar
└── bluecove-gpl-2.1.1.jar
    ├── network/
    │   ├── ClientConnection.java
    │   ├── DataSender.java
    │   ├── DataReceiver.java
    │   └── FileUtils.java

    ├── services/
    │   ├── ClientService.java
    │   ├── SymptomService.java
    │   ├── SignalService.java
    │   └── FileService.java

    ├── pojos/
    │   ├── Client.java
    │   ├── User.java
    │   ├── Signal.java
    │   ├── TypeSignal.java
    │   └── Symptoms.java

    └── utils/
        ├── UIUtils.java
        ├── SecurityUtils.java
        ├── BITalinoUtils.java
        └── DateUtils.java
```
## GUIDE
1- Open the project 
2- Run: main.MainClient
3- Introduce the server IP 
4- User authentication (log in with your username and password or register as a new client)
5- Menu Options: 
a) View medicalHistory
b) Enter symptoms
c) Record signals (EMG/ECG). Connect to the BITalino by the MAC address
6- Disconnect

## AUTTHORS 
- Martina Zandio
- Ana Losada
- Jimena Aineto
- Paula Reyero
- Sara Menor
