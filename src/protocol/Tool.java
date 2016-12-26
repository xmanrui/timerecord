package protocol;

import java.util.ArrayList;

public class Tool {
    enum IntEnum{
        HIGH_8BIT,
        LOW_8BIT,
    }

    private static byte VALUE_0X7E = 0x7e;
    private static byte VALUE_0X7D = 0x7d;
    private static byte VALUE_0X02 = 0x02;
    private static byte VALUE_0X01 = 0x01;

    public static byte IntToByte(int value, IntEnum em)
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

    public static byte[] Escape(byte value)
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

    public static void AddBytesToArrList(byte[] byteArr, ArrayList list)
    {
        for ( byte i: byteArr)
        {
            list.add(i);
        }
    }

    public static void AddByteToArrList(byte value, ArrayList list)
    {
        list.add(value);
    }

    public static void AddLowByteToArrList(int value, ArrayList list)
    {
        byte lowBytes = IntToByte(value, IntEnum.LOW_8BIT);
        byte[] afterChanged = Escape(lowBytes);
        AddBytesToArrList(afterChanged, list);
    }

    // 低字节放前面，高字节放后面
    public static void AddHighAndLowByteToArrList(int value, ArrayList list)
    {
        byte lowByte = IntToByte(value, IntEnum.LOW_8BIT);
        byte highByte = IntToByte(value, IntEnum.HIGH_8BIT);

        AddByteToArrList(lowByte, list);
        AddByteToArrList(highByte, list);
    }

    public static byte[] PackageByteList(ArrayList list)
    {
        byte[] packgeBytes = new byte[list.size()];

        for(int i=0; i<list.size(); i++)
        {
            packgeBytes[i] = (byte)list.get(i);
        }

        return packgeBytes;
    }
}

