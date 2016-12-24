package protocol;

import java.util.ArrayList;
import static protocol.Tool.ChangeByte;
import static protocol.Tool.IntEnum;
import static protocol.Tool.IntToByte;
import static protocol.Tool.AddBytesToArrList;
import static protocol.Tool.AddLowByteToArrList;
import static protocol.Tool.AddHighAndLowByteToArrList;
import static protocol.Tool.PackageByteList;

public abstract class MsgBody {
    public int msgID = 0;
    public ArrayList msgBodyBytesList;

    public MsgBody(int msgID)
    {
        this.msgID = msgID;
    }

    public abstract void MakeMsgBodyByteList();

    public byte[] GetMsgHeaderPackage()
    {
        return PackageByteList(msgBodyBytesList);
    }
}

class ClientCommonAckMsgBody extends MsgBody
{
    public int ackNum = 0;
    public int ackID = 0;
    public int resulte = 0;

    public ClientCommonAckMsgBody()
    {
        super(0x0001);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
        AddHighAndLowByteToArrList(ackNum, msgBodyBytesList);
        AddHighAndLowByteToArrList(ackID, msgBodyBytesList);
        AddLowByteToArrList(resulte, msgBodyBytesList);
    }
}

class ServerCommonAckMsgBody extends MsgBody
{
    public int ackNum = 0;
    public int ackID = 0;
    public int resulte = 0;

    public ServerCommonAckMsgBody()
    {
        super(0x8001);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
        AddHighAndLowByteToArrList(ackNum, msgBodyBytesList);
        AddHighAndLowByteToArrList(ackID, msgBodyBytesList);
        AddLowByteToArrList(resulte, msgBodyBytesList);
    }
}

class ClientHeartMsgBody extends MsgBody
{
    public ClientHeartMsgBody()
    {
        super(0x0002);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
    }
}

class ReSendSubpackageRequestMsgBody extends MsgBody
{
    public int originalMsgNum = 0;
    public int subpackageCount = 0;
    public ArrayList subpackageIDList;

    public ReSendSubpackageRequestMsgBody()
    {
        super(0x8003);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
        AddHighAndLowByteToArrList(originalMsgNum, msgBodyBytesList);
        AddLowByteToArrList(subpackageCount, msgBodyBytesList);

        for(int i=0; i<subpackageIDList.size(); i++)
        {
            AddHighAndLowByteToArrList((int)subpackageIDList.get(i), msgBodyBytesList);
        }
    }
}