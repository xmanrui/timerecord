package protocol;

import java.util.ArrayList;


public final class MsgHeader {
    enum IntEnum{
            HIGH_8BIT,
            LOW_8BIT,
    }
    private static int SUBPACKAGE_FLAG_BIT_INDEX = 13;
    private static int PHONENUM_LEN = 16;
    private static byte VALUE_0X7E = 0x7e;
    private static byte VALUE_0X7D = 0x7d;
    private static byte VALUE_0X02 = 0x02;
    private static byte VALUE_0X01 = 0x01;

    public int protocolVersion = 0;
    public int msgID = 0;
    public int msgBodyProperty = 0;
    public int[] phoneNum = new int[PHONENUM_LEN];
    public int msgIndex = 0;
    public int reserved = 0;
    public int packetCount = 0;
    public int packageIndex = 0;

    public ArrayList msgHeaderByteList = new ArrayList();

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

    private byte IntToByte(int value, IntEnum em)
    {
        byte byteValue = 0;

        if(em == IntEnum.LOW_8BIT)
        {
            byteValue = (byte)(value & 0xFF);
        }
        else if (em == IntEnum.HIGH_8BIT)
        {
            byteValue = (byte)((value >> 8) & 0xFF);
        }

        return byteValue;
    }

    private byte[] ChangeByte(byte value)
    {
        byte[] afterChanged;

        if (value == VALUE_0X7E)
        {
            afterChanged = new byte[2];
            afterChanged[0] = VALUE_0X7E;
            afterChanged[1] = VALUE_0X02;
        }
        else if (value == VALUE_0X7D)
        {
            afterChanged = new byte[2];
            afterChanged[0] = VALUE_0X7D;
            afterChanged[1] = VALUE_0X01;
        }
        else
        {
            afterChanged = new byte[1];
            afterChanged[0] = value;
        }

        return afterChanged;
    }
    private void AddBytesToArrList(byte[] byteArr)
    {
        for ( byte i: byteArr)
        {
            msgHeaderByteList.add(i);
        }
    }

    private void AddLowByteToArrList(int value)
    {
        byte lowBytes = IntToByte(value, IntEnum.LOW_8BIT);
        byte[] afterChanged = ChangeByte(lowBytes);
        AddBytesToArrList(afterChanged);
    }

    // 高字节放前面，低字节放后面
    private void AddHighAndLowByteToArrList(int value)
    {
        byte highByte = IntToByte(value, IntEnum.HIGH_8BIT);
        byte lowByte = IntToByte(value, IntEnum.LOW_8BIT);
        byte[] highByteArr = ChangeByte(highByte);
        byte[] lowByteArr = ChangeByte(lowByte);

        AddBytesToArrList(highByteArr);
        AddBytesToArrList(lowByteArr);
    }

    // numTwo放在高4位
    private byte ChangeTwoPhoneNumToAByte(int numOne, int numTwo)
    {
        byte numOneLowBits = IntToByte(numOne, IntEnum.LOW_8BIT);
        byte numTwoLowBits = IntToByte(numTwo, IntEnum.LOW_8BIT);

        byte afterMerged = (byte)( ((numTwoLowBits & 0x0F) << 4) & (numOneLowBits & 0x0F) );

        return afterMerged;
    }

    private void AddPhoneNumToArrList()
    {
        byte byteValue = 0;
        byte[] afterChanged;
        for(int i=0; i<phoneNum.length; i += 2)
        {
            byteValue = ChangeTwoPhoneNumToAByte(phoneNum[i], phoneNum[i+1]);
            afterChanged = ChangeByte(byteValue);
            AddBytesToArrList(afterChanged);
        }
    }

    private void AddMsgHeaderEndToArrList()
    {
        AddHighAndLowByteToArrList(packetCount);
        AddHighAndLowByteToArrList(packageIndex);
    }

    public void MakeMsgHeaderByteList()
    {
        AddLowByteToArrList(protocolVersion);
        AddHighAndLowByteToArrList(msgID);
        AddHighAndLowByteToArrList(msgBodyProperty);
        AddPhoneNumToArrList();
        AddHighAndLowByteToArrList(msgIndex);
        AddHighAndLowByteToArrList(reserved);

        if(IsLongMsg())
        {
            AddMsgHeaderEndToArrList();
        }
    }

    public byte[] GetMsgHeaderPackage()
    {
        byte[] packgeBytes = new byte[msgHeaderByteList.size()];

        for(int i=0; i<msgHeaderByteList.size(); i++)
        {
            packgeBytes[i] = (byte)msgHeaderByteList.get(i);
        }

        return packgeBytes;
    }

}
