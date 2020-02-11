/*
 * Created on 01-Mar-2016
 */
package udp;

import java.io.IOException;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.Arrays;
import java.io.ByteArrayInputStream;
import common.MessageInfo;

public class UDPServer {

    private DatagramSocket recvSoc;
    private int totalMessages = -1;
    private int[] receivedMessages;
    private boolean close;

    private void run() {
	    
	int pacSize;
	byte[] pacData;
	DatagramPacket pac;
		

	System.out.println("Thread created");

	while(true){
	    try{
		recvSoc.setSoTimeout(10*1000);

		pacData = new byte[1000];
		pacSize = pacData.length;

		DatagramPacket request = new DatagramPacket(pacData, pacSize);

		recvSoc.receive(request);

		ByteArrayInputStream in = new ByteArrayInputStream(pacData);
		ObjectInputStream is = new ObjectInputStream(in);
		MessageInfo msg = (MessageInfo) is.readObject();
		String data = msg.toString();
		processMessage(data);
	
	    }

	    catch(SocketTimeoutException e) {
		System.out.println("Timed out. Quit process.");
		printMissing();
		
		break;
	    }
			
	    catch(ClassNotFoundException e){
		System.out.println("Class not found.");
	    }
	    catch(IOException e){
		System.out.println("IO exception.");
	    }
	    catch(Exception e){
		System.out.println("General exception in UDPSERVER run().");
	    }

	}

	if(totalMessages == -1) {
	    System.out.println("No Messages received.");
	}

	return;
	// TO-DO: Receive the messages and process them by calling processMessage(...).
	//        Use a timeout (e.g. 30 secs) to ensure the program doesn't block forever

    }

    public void processMessage(String data) {

	MessageInfo msg = null;

	try {
	    msg = new MessageInfo(data);
	}
	catch(Exception e){
	    System.out.println("error in processMessage");
	}

	if(msg.messageNum == 0) {
	    totalMessages = msg.totalMessages;
	    receivedMessages = new int[totalMessages];
	}

	receivedMessages[msg.messageNum] = 1;

	if(msg.messageNum==msg.totalMessages-1){

	    printMissing();
	    System.exit(0);
	}
    }
    // TO-DO: Use the data to construct a new MessageInfo object

    // TO-DO: On receipt of first message, initialise the receive buffer

    // TO-DO: Log receipt of the message

    // TO-DO: If this is the last expected message, then identify
    //        any missing messages



    public UDPServer(int rp) {
	// TO-DO: Initialise UDP socket for receiving data
	try{
	    recvSoc = new DatagramSocket(rp);
	}
	catch (SocketException e){
	    System.out.println("Socket: " + e.getMessage());
	}
	// Done Initialisation
	System.out.println("UDPServer ready");
    }


    private void printMissing(){

	    
	int count = 0;
	
	for(int i = 0; i<totalMessages;i++) {
	    if (receivedMessages[i] == 0) {

		count++;
		System.out.println(i + " ");
	    }
	}

	if (count == 0){
	    System.out.println("All messages received");
	    return;
	}
	
	System.out.println("Nr of missing messages: " + count);
	return;
    }

    
    public static void main(String args[]) {
	int	recvPort;

	// Get the parameters from command line
	if (args.length < 1) {
	    System.err.println("Arguments required: recv port");
	    System.exit(-1);
	}
	recvPort = Integer.parseInt(args[0]);

	UDPServer udpServer = new UDPServer(recvPort);
	udpServer.run();
	// TO-DO: Construct Server object and start it by calling run()

    }
}
