import PrimeApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.Scanner;

public class PrimeClient
{
    public static void main(String args[])
    {
        try
        {
            ORB orb = ORB.init(args, null);

            org.omg.CORBA.Object objRef =
            orb.resolve_initial_references("NameService");

            NamingContextExt ncRef =
            NamingContextExtHelper.narrow(objRef);

            Prime obj =
            PrimeHelper.narrow(
            ncRef.resolve_str("Prime"));

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter Number: ");
            int n = sc.nextInt();

            System.out.println(
            obj.checkPrime(n));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}