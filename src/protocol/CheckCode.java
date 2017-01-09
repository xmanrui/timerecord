package protocol;


public final class CheckCode {
    public static byte MakeCheckedCode(byte[] msgHeader, byte[] msgBody)
    {
        byte temp = msgHeader[0];

        for(int i=1; i < msgHeader.length; i++)
        {
            temp ^= msgHeader[i];
        }

        for(int i=0; i<msgBody.length; i++)
        {
            temp ^= msgBody[i];
        }
        return temp;
    }

    public static byte MakeCheckedCode(byte[] arr)
    {
        if (arr.length == 0)
        {
            return (byte)0;
        }

        byte temp = arr[0];

        for(int i=1; i<arr.length; i++)
        {
            temp ^= arr[i];
        }

        return temp;
    }
}
