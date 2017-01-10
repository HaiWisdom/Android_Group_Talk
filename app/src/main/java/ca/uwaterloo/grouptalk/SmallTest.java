package ca.uwaterloo.grouptalk;

import org.apache.thrift.TException;

/**
 * Created by Wisdom H Jiang on 2016-11-21.
 */

public class SmallTest {
    public static void main(String [ ] args) throws TException
    {
        ClientX client = new ClientX();
        MessageService.Client mClient = client.getThriftClient();
        System.out.println(mClient.loginX("test"));
        try {
            Thread.sleep(20000);
        }catch(InterruptedException e){}
    }
}
