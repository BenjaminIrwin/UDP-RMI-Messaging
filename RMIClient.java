/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import java.rmi.*;

import java.net.MalformedURLException;

import common.MessageInfo;

public class RMIClient {

	// A typical client program obtains a remote reference to one or more remote objects on a server
	// and then invokes methods on them.

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		// Check arguments for Server host and number of messages
		if (args.length < 2) {
			System.err.println("Clientside Error: needs 2 arguments: ServerHostName/IPAddress, TotalMessageCount");
			System.exit(-1);
		}

		String urlServer = new String("rmi://" + args[0] + "/RMIServer");
		int numMessages = Integer.parseInt(args[1]);

		// TO-DO: Initialise Security Manager
		if(System.getSecurityManager() == null) {
			System.setSecurityManager(new SecurityManager());
		}

		// TO-DO: Bind to RMIServer
		try {
			iRMIServer = (RMIServerI) Naming.lookup(urlServer);
		} catch (MalformedURLException e) {
			System.err.println("Clientside Error: malformed server URL.");
			System.exit(-1);
		} catch (NotBoundException e) {
			System.err.println("Clientside Error: client-server bind failed.");
			System.exit(-1);
		} catch (RemoteException e) {
			System.err.println("Clientside Error: client-server communication failed.");
			System.exit(-1);
		}

		// TO-DO: Attempt to send messages the specified number of times
		for (int i = 0; i < numMessages; i++) {
			MessageInfo msg = new MessageInfo(numMessages, i);
			System.out.println("printing numesages an i")
			System.out.println(numMessages);
			System.out.println(i);

			try {
				iRMIServer.receiveMessage(msg);
			} catch (RemoteException e) {
				System.err.println("Clientside Error: could not send message.");
				System.exit(-1);
			}

		}
		System.out.println("normal exit")
		System.exit(0);

	}
}
