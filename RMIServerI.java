/*
 * Created on 01-Mar-2016
 */
package rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

import common.*;

// Clients program to remote interfaces, not to the implementation classes of those interfaces.

public interface RMIServerI extends Remote {
	public void receiveMessage(MessageInfo msg) throws RemoteException;

	public void getMessageInfo();
}
