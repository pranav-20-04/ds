import java.rmi.*;

interface ServerIntf extends Remote {

    public int addition(int a, int b) throws RemoteException;

    public int substraction(int a, int b) throws RemoteException;

    public int division(int a, int b) throws RemoteException;

    public int multiplication(int a, int b) throws RemoteException;

    public int square(int a) throws RemoteException;

    public int squareroot(int a) throws RemoteException;

    public int cube(int a) throws RemoteException;

    public String palindrome(String str) throws RemoteException;

    public String isequalstring(String str1, String str2) throws RemoteException;

}
