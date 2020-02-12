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

//User defined RMI error exit codes:
//1 - Insufficient command line arguments
//2 - Malformed URL
//3 - Bind failure
//4 - Communication failure (remote exception)
//5 - Server creation failure
//6 - Invalid message number

public class RMIClient {

	public static void main(String[] args) {

		RMIServerI iRMIServer = null;

		if (args.length < 2) {
			System.err.println("Error (client): needs 2 arguments: " +
					"ServerHostName/IPAddress, TotalMessageCount");
			System.exit(1);
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
			System.err.println("Error (client): malformed server URL.");
			System.exit(2);
		} catch (NotBoundException e) {
			System.err.println("Error (client): client-server bind failed.");
			System.exit(3);
		} catch (RemoteException e) {
			System.err.println("Error (client): client-server communication "
					+ "failed.");
			System.exit(4);
		}

		// TO-DO: Attempt to send messages the specified number of times
		for (int i = 0; i < numMessages; i++) {
			MessageInfo msg = new MessageInfo(numMessages, i);

			try {
				iRMIServer.receiveMessage(msg);
			} catch (RemoteException e) {
				System.err.println("Error (client): could not invoke " +
						"RMIServer's receiveMessage function.");
				System.exit(4);
			}

		}

		try {
			iRMIServer.getMessageInfo();
		} catch (RemoteException e) {
			System.err.println("Error (client): could not invoke " +
					"RMIServer's getMessageInfo function.");
			System.exit(4);
		}

		System.exit(0);

	}
}
