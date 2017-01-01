package protocol;

import java.util.ArrayList;
import static protocol.Tool.*;
import static protocol.Tool.StrChangeToBytes;

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

class ClientRegisterMsgBody extends MsgBody
{
    public int provinceID = 0;
    public int cityID = 0;
    public int[] manufacturerID = new int[5];
    public int[] clientType = new int[20];
    public int[] serialNumber = new int[7];
    public int[] IMEI = new int[15];
    public int carColor = 0;
    public String carID = "";

    public ClientRegisterMsgBody()
    {
        super(0x0100);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
        AddLowByteToArrList(provinceID, msgBodyBytesList);
        AddLowByteToArrList(cityID, msgBodyBytesList);
        AddLowBytesToArrList(manufacturerID, msgBodyBytesList);
        AddLowBytesToArrList(clientType, msgBodyBytesList);
        AddLowBytesToArrList(serialNumber, msgBodyBytesList);
        AddLowBytesToArrList(IMEI, msgBodyBytesList);
        AddLowByteToArrList(carColor, msgBodyBytesList);
        AddBytesToArrList(StrChangeToBytes(carID), msgBodyBytesList);
    }
}

class ClientRegisterAckMsgBody extends MsgBody
{
    int ackID = 0;
    int result = 0;
    int[] platformID = new int[5];
    int[] trainingInstitutionsID =  new int[16];
    int[] timeClientID = new int[16];
    String clientCertificate = "";

    public ClientRegisterAckMsgBody()
    {
        super(0x8100);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
        AddLowByteToArrList(ackID, msgBodyBytesList);
        AddLowByteToArrList(result, msgBodyBytesList);
        AddLowBytesToArrList(platformID, msgBodyBytesList);
        AddLowBytesToArrList(trainingInstitutionsID, msgBodyBytesList);
        AddLowBytesToArrList(timeClientID, msgBodyBytesList);
        AddBytesToArrList(StrChangeToBytes(clientCertificate), msgBodyBytesList);
    }
}

class ClientLogoutMsgBody extends MsgBody
{
    public ClientLogoutMsgBody()
    {
        super(0x0003);
    }

    public void MakeMsgBodyByteList()
    {
        msgBodyBytesList.clear();
    }
}

