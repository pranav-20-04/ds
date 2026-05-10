import PrimeApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class PrimeServer
{
    public static void main(String args[])
    {
        try
        {
            ORB orb = ORB.init(args, null);

            POA rootpoa =
            POAHelper.narrow(
            orb.resolve_initial_references("RootPOA"));

            rootpoa.the_POAManager().activate();

            PrimeImpl obj = new PrimeImpl();

            org.omg.CORBA.Object ref =
            rootpoa.servant_to_reference(obj);

            Prime href =
            PrimeHelper.narrow(ref);

            org.omg.CORBA.Object objRef =
            orb.resolve_initial_references("NameService");

            NamingContextExt ncRef =
            NamingContextExtHelper.narrow(objRef);

            NameComponent path[] =
            ncRef.to_name("Prime");

            ncRef.rebind(path, href);

            System.out.println("Prime Server Started");

            orb.run();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}