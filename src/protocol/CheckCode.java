package protocol;

import static protocol.Tool.ChangeByte;

public final class CheckCode {
    public byte[] MakeCheckedCode(byte[] msgHeader, byte[] msgBody)
    {
        byte temp = msgHeader[0];

        for(int i=1; i < msgHeader.length-1; i++)
        {
            temp ^= msgHeader[i];
        }

        for(int i=0; i<msgBody.length-1; i++)
        {
            temp ^= msgBody[i];
        }
        return ChangeByte(temp);
    }
}
