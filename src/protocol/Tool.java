package protocol;

import java.util.ArrayList;
import static protocol.CheckCode.*;

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

    public static int ByteToInt(byte value)
    {
        return value & 0x000000FF;
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

    public class AcceptByteArry
    {
        private byte[] data;
        private  int start = 0;
        private  int end = 0;
        private  int len = 1024 * 1024 * 10; // 10MB
        private int currentIndex = 0;

        public AcceptByteArry()
        {
            data = new byte[len];

            DataClear();
        }

        private void DataClear()
        {
            for(int i=0; i<len; i++)
            {
                data[i] = 0;
            }
        }

        private void DataClear(int startIndex)
        {
            if (startIndex<0)
            {
                return;
            }
            for(int i=startIndex; i<len; i++)
            {
                data[i] = 0;
            }
        }

        private boolean IsValidSubpackage(byte[] arr)
        {
            boolean isValid = false;

            if(arr.length < 10) // 一个包数据不可能小于10的
            {
               return false;
            }

            byte checkedCode = arr[1];
            for (int i=2; i<arr.length-2; i++)
            {
                checkedCode ^= arr[i];
            }

            if(checkedCode == arr[arr.length-2])
            {
                isValid = true;
            }

            return isValid;
        }

        public byte[] push(byte[] arr)
        {
            for(int i=0; i<arr.length; i++)
            {
                data[currentIndex] = arr[i];
                currentIndex++;
            }

            if (currentIndex < 10) // 一个包数据不可能小于10的
            {
                return new byte[0];
            }

            int subpackageHead = -1;
            int subpackageTail = -1;
            boolean hasPackage = false;
            byte[] subpackage;

            for(int i=0; i<currentIndex; i++)
            {
                if ((data[i] == VALUE_0X7E) && (data[i+1] == VALUE_0X7E))
                {
                    data[i] = 0;
                }

                if (data[i] == VALUE_0X7E)
                {
                    subpackageHead = i;
                }

                if((data[i] == VALUE_0X7E) && (subpackageHead != i))
                {
                    subpackageTail = i;
                    hasPackage = true;
                }
            }

            if (hasPackage)
            {
                int subpackageLen = subpackageTail - subpackageHead;
                subpackage = new byte[subpackageLen];

                for(int i= subpackageHead; i<subpackageTail+1; i++)
                {
                    subpackage[i - subpackageHead] = data[i];
                }

                if(IsValidSubpackage(subpackage))
                {
                    int copyLen = currentIndex - subpackageTail - 1;
                    for (int i=0; i<copyLen; i++)
                    {
                        data[i] = data[subpackageTail + i + 1];
                    }

                    DataClear(copyLen);
                    currentIndex = copyLen;
                }
                else
                {
                    data[0] = VALUE_0X7E;// 无效包把包的标记留下
                    int copyLen = currentIndex - subpackageTail - 1;
                    int offset = 1;

                    for (int i=offset; i<copyLen; i++)
                    {
                        data[i] = data[subpackageTail + i + 1];
                    }

                    int startIndex = copyLen + offset;
                    DataClear(startIndex);
                    currentIndex = startIndex;

                    return new byte[0];
                }
            }
            else
            {
                subpackage = new byte[0];
            }

            return subpackage;
        }

        public byte[] push(byte value)
        {

            data[currentIndex] = value;
            currentIndex++;

            if (currentIndex < 10) // 一个包数据不可能小于10的
            {
                return new byte[0];
            }

            int subpackageHead = -1;
            int subpackageTail = -1;
            boolean hasPackage = false;
            byte[] subpackage;

            for(int i=0; i<currentIndex; i++)
            {
                if ((data[i] == VALUE_0X7E) && (data[i+1] == VALUE_0X7E))
                {
                    data[i] = 0;
                }

                if (data[i] == VALUE_0X7E)
                {
                    subpackageHead = i;
                }

                if((data[i] == VALUE_0X7E) && (subpackageHead != i))
                {
                    subpackageTail = i;
                    hasPackage = true;
                }
            }

            if(hasPackage)
            {
                int subpackageLen = subpackageTail - subpackageHead;
                subpackage = new byte[subpackageLen];

                for(int i= subpackageHead; i<subpackageTail+1; i++)
                {
                    subpackage[i - subpackageHead] = data[i];
                }

                if(IsValidSubpackage(subpackage))
                {
                    DataClear();
                    currentIndex = 0;
                }
                else // 无效包把包的标记留下
                {
                    DataClear();
                    data[0] = VALUE_0X7E;
                    currentIndex = 1;
                    return new byte[0];
                }
            }
            else
            {
                subpackage = new byte[0];
            }


            return subpackage;
        }
    }

    public class SendData
    {
        public int count = 0;
        public byte[] msgPackage;
        public byte[] msgHeader;
        public long nextTime = 0;
        public long currentTime = 0;
        public SendData(byte[] msgHeader, byte[] msgPackage)
        {
            this.msgHeader = msgHeader.clone();
            this.msgPackage = msgPackage.clone();
        }
    }
}

