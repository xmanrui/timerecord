package protocol;

public class PackageTool {
    private static byte PREFIX = 0x7e;
    private static byte SUFFIX = 0x7e;

    public static byte[] PackMsg(byte[] msgHeader, byte[] msgBody, byte[] checkedCode)
    {
        int msgHeaderLen = msgHeader.length;
        int msgBodyLen = msgBody.length;
        int checkedCodeLen = checkedCode.length;
        int len = 1 + 1 + msgHeaderLen + msgBodyLen + checkedCodeLen;
        byte[] afterPacked = new byte[len];

        afterPacked[0] = PREFIX;
        afterPacked[len-1] = SUFFIX;

        int offset = 1;
        for(int i=0; i<msgHeaderLen-1; i++)
        {
            afterPacked[i + offset] = msgHeader[i];
        }

        offset = 1 + msgHeaderLen;
        for(int i=0; i<msgBodyLen-1; i++)
        {
            afterPacked[i + offset] = msgBody[i];
        }

        offset = 1 + msgBodyLen;
        for(int i=0; i<checkedCodeLen-1; i++)
        {
            afterPacked[i + offset] = checkedCode[i];
        }

        return afterPacked;
    }

}
