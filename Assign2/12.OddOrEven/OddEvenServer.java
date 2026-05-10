import OddEvenApp.*;
import org.omg.CosNaming.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;

public class OddEvenServer
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

            OddEvenImpl obj = new OddEvenImpl();

            org.omg.CORBA.Object ref =
            rootpoa.servant_to_reference(obj);

            OddEven href =
            OddEvenHelper.narrow(ref);

            org.omg.CORBA.Object objRef =
            orb.resolve_initial_references("NameService");

            NamingContextExt ncRef =
            NamingContextExtHelper.narrow(objRef);

            NameComponent path[] =
            ncRef.to_name("OddEven");

            ncRef.rebind(path, href);

            System.out.println("Odd Even Server Started");

            orb.run();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
    }
}