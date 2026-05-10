import CalculatorApp.*;
import org.omg.CORBA.*;

public class CalculatorImpl extends CalculatorPOA
{
    public int add(int a, int b)
    {
        return a + b;
    }
    public int sub(int a , int b){
        return a - b;
    }
}