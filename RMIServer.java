/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.*;

import common.*;

// A typical server program creates some remote objects, makes references to these objects accessible,
// and waits for clients to invoke methods on these objects.

public class RMIServer extends UnicastRemoteObject implements RMIServerI {

	private int totalMessages = -1;
	private int[] receivedMessages;

	public RMIServer() throws RemoteException {
		super();
	}

	public void receiveMessage(MessageInfo msg) throws RemoteException {

		// TO-DO: On receipt of first message, initialise the receive buffer
		if (msg.messageNum == 0) {
			receivedMessages = new int[msg.totalMessages];
			for (int i = 0; i < msg.totalMessages; i++) {
				receivedMessages[i] = -1;
			}

			totalMessages = msg.totalMessages;
		}

		// TO-DO: Log receipt of the message
		receivedMessages[msg.messageNum] = 1;
		System.out.println("Received Message " + msg.messageNum + " of " msg.totalMessages);

		// TO-DO: If this is the last expected message, then identify
		//        any missing messages
		if (msg.messageNum == totalMessages) {

			int count = 0;

			for (int i = 0; i < totalMessages; i++) {
				if (receivedMessages[i] == 0) {
					count++;
				}
			}

		}
	}

	public static void main(String[] args) {

		// TO-DO: Initialise Security Manager
			if(System.getSecurityManager() == null) {
				System.setSecurityManager(new SecurityManager());
			}

		RMIServer rmis = null;

			try {
				// TO-DO: Instantiate the server class
				rmis = new RMIServer();
			}
			catch (Exception e) {
				System.err.println("Server-side Error: could not create new RMIServer.");
			}

		rebindServer("RMIServer", rmis);

		// TO-DO: Bind to RMI registry.
			// Can use various mechanisms to obtain references to remote objects.
			// For example, an application can register its remote objects with RMI's simple naming facility, the RMI registry.

		}

	protected static void rebindServer(String serverURL, RMIServer server) {

		// TO-DO:
		// Start / find the registry (hint use LocateRegistry.createRegistry(...)
		// If we *know* the registry is running we could skip this (eg run rmiregistry in the start script)

		// TO-DO:
		// Now rebind the server to the registry (rebind replaces any existing servers bound to the serverURL)
		// Note - Registry.rebind (as returned by createRegistry / getRegistry) does something similar but
		// expects different things from the URL field.
		Registry r = null;

		try {
			r = LocateRegistry.createRegistry(1099);//If can't create try get and if can't get then fails
		} catch (RemoteException e) {
			try {
				r = LocateRegistry.getRegistry();
			} catch (RemoteException e1){
				System.err.println("Server-side Error: could not create registry for rebinding.");
				System.exit(-1);
			}
		}

		try {
			Naming.rebind(serverURL, server);
		} catch (RemoteException e) {
			System.err.println("Server-side Error: could not rebind.");
			System.exit(-1);
		} catch (MalformedURLException e) {
			System.err.println("Server-side Error: could not rebind with malformed URL.");
			System.exit(-1);
		}
	}
}
