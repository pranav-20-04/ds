import CalculatorApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import java.util.Scanner;

public class CalculatorClient
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

            Calculator obj =
            CalculatorHelper.narrow(
            ncRef.resolve_str("Calculator"));

            Scanner sc = new Scanner(System.in);

            System.out.print("Enter First Number: ");
            int a = sc.nextInt();

            System.out.print("Enter Second Number: ");
            int b = sc.nextInt();

            System.out.println(
            "Addition = " + obj.add(a, b));

            System.out.println("Subtraction = " + obj.sub(a,b));
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}