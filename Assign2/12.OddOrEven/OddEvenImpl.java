import OddEvenApp.*;

public class OddEvenImpl extends OddEvenPOA
{
    public String check(int n)
    {
        if(n % 2 == 0)
            return "Even";
        else
            return "Odd";
    }
}