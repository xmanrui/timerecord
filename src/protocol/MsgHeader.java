package protocol;

import java.util.ArrayList;

import static protocol.Tool.IntEnum;
import static protocol.Tool.IntToByte;
import static protocol.Tool.AddByteToArrList;
import static protocol.Tool.AddLowByteToArrList;
import static protocol.Tool.AddHighAndLowByteToArrList;
import static protocol.Tool.PackageByteList;

public final class MsgHeader {
    private static int SUBPACKAGE_FLAG_BIT_INDEX = 13;
    private static int PHONENUM_LEN = 16;

    public int protocolVersion = 0;
    public int msgID = 0;
    public int msgBodyProperty = 0;
    public int[] phoneNum = new int[PHONENUM_LEN];
    public int msgIndex = 0;
    public int reserved = 0;
    public int packetCount = 0;
    public int packageIndex = 0;

    public ArrayList msgHeaderByteList = new ArrayList();

    public MsgHeader(int msgID){
        this.msgID = msgID;
        for(int i=0; i<phoneNum.length; i++)
        {
            phoneNum[i] = 0;
        }
    }

    public void MakeMsgBodyProperty(int subpackageFlag, int encryptType, int msgBodyLenght)
    {
        int mbProperyt = 0;
        mbProperyt = subpackageFlag << SUBPACKAGE_FLAG_BIT_INDEX;
        mbProperyt = mbProperyt | (encryptType << 12);
        mbProperyt = mbProperyt | msgBodyLenght;
        msgBodyProperty = mbProperyt;
    }

    private boolean IsLongMsg()
    {
        boolean flag = false;

        if((msgBodyProperty >> SUBPACKAGE_FLAG_BIT_INDEX) == 1)
        {
            flag = true;
        }

        return flag;
    }

    // numTwo放在高4位
    private byte ChangeTwoPhoneNumToAByte(int numOne, int numTwo)
    {
        byte numOneLowBits = IntToByte(numOne, IntEnum.LOW_8BIT);
        byte numTwoLowBits = IntToByte(numTwo, IntEnum.LOW_8BIT);

        byte afterMerged = (byte)( ((numTwoLowBits & 0x0F) << 4) & (numOneLowBits & 0x0F) );

        return afterMerged;
    }

    private void AddPhoneNumToArrList(ArrayList list)
    {
        byte byteValue = 0;
        for(int i=0; i<phoneNum.length; i += 2)
        {
            byteValue = ChangeTwoPhoneNumToAByte(phoneNum[i], phoneNum[i+1]);
            AddByteToArrList(byteValue, list);
        }
    }

    private void AddMsgHeaderEndToArrList(ArrayList list)
    {
        AddHighAndLowByteToArrList(packetCount, list);
        AddHighAndLowByteToArrList(packageIndex, list);
    }

    public void MakeMsgHeaderByteList()
    {
        msgHeaderByteList.clear();
        AddLowByteToArrList(protocolVersion, msgHeaderByteList);
        AddHighAndLowByteToArrList(msgID, msgHeaderByteList);
        AddHighAndLowByteToArrList(msgBodyProperty, msgHeaderByteList);
        AddPhoneNumToArrList(msgHeaderByteList);
        AddHighAndLowByteToArrList(msgIndex, msgHeaderByteList);
        AddHighAndLowByteToArrList(reserved, msgHeaderByteList);

        if(IsLongMsg())
        {
            AddMsgHeaderEndToArrList(msgHeaderByteList);
        }
    }

    public byte[] GetMsgHeaderPackage()
    {
        return PackageByteList(msgHeaderByteList);
    }

}
