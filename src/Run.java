import java.util.ArrayList;

/**
 * Created by pc on 2016/12/20.
 */
public class Run {
    public static void testfun(ArrayList aList)
    {

        testfun2(aList);
    }

    public static void testfun2(ArrayList aList)
    {
        for (int i=0; i<10; i++)
        {
            byte t = (byte)i;
            aList.add(t);
        }
    }
    public static void main(String[] args) {
    System.out.println("xmryxx");
    int b =  (1024 >> 9);
        System.out.println(b);

        ArrayList lt = new ArrayList();
        lt.add((byte)101);
        testfun(lt);

        lt.add(0, (byte)111);

        for (int i=0; i<lt.size(); i++)
        {
            byte aaa= (byte)lt.get(i);
            System.out.println(aaa);
        }

        byte[] atest = new byte[0];
        System.out.println(atest.length);

        String strTest = new String("ffdfd");
        byte[] bTest = strTest.getBytes();
        System.out.println(bTest.length);
        byte aaaa = (byte)127;
        int aaa = (int)(aaaa & 0x000000FF);
        System.out.println(aaa);

    }
}

