import java.util.ArrayList;

/**
 * Created by pc on 2016/12/20.
 */
public class Run {
    public static void main(String[] args) {
    System.out.println("xmryxx");
    int a = 1;
    int b =  (1024 >>9);
        System.out.println(b);

        ArrayList lt = new ArrayList();
        for (int i=0; i<16; i++)
        {
            byte t = (byte)i;
            lt.add(t);
        }

        for (int i=0; i<lt.size(); i++)
        {
            byte aaa= (byte)lt.get(i);
            System.out.println(aaa);
        }
    }
}
