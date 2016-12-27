package protocol;

import java.util.ArrayList;
import static protocol.CheckCode.MakeCheckedCode;
import static protocol.Tool.Escape;
import static protocol.Tool.AddBytesToArrList;
import static protocol.Tool.VALUE_0X7E;

public class PackageTool {

    public static byte[] PackMsg(byte[] msgHeader, byte[] msgBody)
    {
        ArrayList list = new ArrayList();
        list.add(VALUE_0X7E);

        byte[] afterEscape;

        for(int i=0; i<msgHeader.length; i++)
        {
            afterEscape = Escape(msgHeader[i]);
            AddBytesToArrList(afterEscape, list);
        }

        for(int i=0; i<msgBody.length; i++)
        {
            afterEscape = Escape(msgBody[i]);
            AddBytesToArrList(afterEscape, list);
        }

        byte checkCode = MakeCheckedCode(msgHeader, msgBody);
        afterEscape = Escape(checkCode);
        AddBytesToArrList(afterEscape, list);

        list.add(VALUE_0X7E);

        byte[] afterPacked = new byte[list.size()];

        for(int i=0; i<list.size(); i++)
        {
            afterPacked[i] = (byte)list.get(i);
        }

        return afterPacked;
    }

}
