package protocol;

/**
 * Created by pc on 2016/12/23.
 */
public final class CheckCode {
    public byte MakeCheckedCode(byte[] byteArr)
    {
        byte afterChecked = 0;
        if(byteArr.length == 0)
        {
            return afterChecked;
        }

        afterChecked = byteArr[0];

        for(int i=1; i < byteArr.length; i++)
        {
            afterChecked ^= byteArr[i];
        }

        return afterChecked;
    }
}
