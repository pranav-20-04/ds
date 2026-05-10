import StringApp.*;

public class StringOperationImpl
extends StringOperationPOA
{
    public String reverse(String str)
    {
        String rev = "";

        for(int i = str.length()-1; i >= 0; i--)
        {
            rev += str.charAt(i);
        }

        return rev;
    }
}