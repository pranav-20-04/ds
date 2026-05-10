import PrimeApp.*;

public class PrimeImpl extends PrimePOA
{
    public String checkPrime(int n)
    {
        int flag = 0;

        for(int i = 2; i <= n/2; i++)
        {
            if(n % i == 0)
            {
                flag = 1;
                break;
            }
        }

        if(flag == 0)
            return "Prime";
        else
            return "Not Prime";
    }
}