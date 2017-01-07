package protocol;

import java.util.ArrayList;

public class Tool {
    enum IntEnum{
        HIGH_8BIT,
        LOW_8BIT,
    }

    protected static byte VALUE_0X7E = 0x7e;
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
        byte lowByte = IntToByte(value, IntEnum.LOW_8BIT);
        AddByteToArrList(lowByte, list);
    }

    public static void AddLowBytesToArrList(int[] value, ArrayList list)
    {
        for(int i: value)
        {
            AddLowByteToArrList(i, list);
        }
    }

    // 低字节放前面，高字节放后面
    public static void AddHighAndLowByteToArrList(int value, ArrayList list)
    {
        byte lowByte = IntToByte(value, IntEnum.LOW_8BIT);
        byte highByte = IntToByte(value, IntEnum.HIGH_8BIT);

        AddByteToArrList(lowByte, list);
        AddByteToArrList(highByte, list);
    }

    public static void AddHighAndLowBytesToArrList(int[] value, ArrayList list)
    {
        for(int i: value)
        {
            AddHighAndLowByteToArrList(i, list);
        }
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

    public static byte[] StrToBytes(String str)
    {
        byte[] originalBytes = str.getBytes(); // ? gbk或utf-8字符集
        byte[] afterChangedBytes = new byte[originalBytes.length + 1];
        for(int i=0; i<originalBytes.length; i++)
        {
            afterChangedBytes[i] = originalBytes[i];
        }

        afterChangedBytes[afterChangedBytes.length + 1] = 0; // 最后一个位置填0

        return afterChangedBytes;
    }

    public static byte[] LongToBytes(long value)
    {
        byte[] afterChanged = new byte[4];
        afterChanged[0] = (byte)(value & 0xFF);
        afterChanged[1] = (byte)((value >> 8) & 0xFF);
        afterChanged[2] = (byte)((value >> 16) & 0xFF);
        afterChanged[3] = (byte)((value >> 24) & 0xFF);
        return afterChanged;
    }

    public class CycleByteArray
    {
        private byte[] data;
        private  int startIndex = 0;
        private  int endIndex = 0;
        private  int currentIndex = -1;
        private  int len = 1024 * 1024 * 10; // 10MB

        public CycleByteArray()
        {
            data = new byte[len];

            for(int i=0; i<len; i++)
            {
                data[i] = 0;
            }

        }

        public void push(byte value)
        {
            currentIndex++;
            data[currentIndex] = value;
        }
    }
}

