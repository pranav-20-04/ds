import StringApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.Scanner;

public class StringClient
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

            StringOperation obj =
            StringOperationHelper.narrow(
            ncRef.resolve_str("StringOperation"));

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter String: ");
            String str = sc.nextLine();

            System.out.println(
            "Reversed String = " +
            obj.reverse(str));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}