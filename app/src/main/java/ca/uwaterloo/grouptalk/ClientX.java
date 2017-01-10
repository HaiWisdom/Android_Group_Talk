package ca.uwaterloo.grouptalk;

import android.util.Log;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;

import static android.content.ContentValues.TAG;

/**
 * Created by Wisdom H Jiang on 2016-11-07.
 */

public class ClientX {
    private String host;
    private int port;

    public ClientX(){
        host = "ec2-35-161-117-237.us-west-2.compute.amazonaws.com";
        port = 8888;
    }

    public MessageService.Client getThriftClient(){
        int tryTime = 0;
        while (true){
            try {
                TSocket sock = new TSocket(host,port);
                TTransport transport = new TFramedTransport(sock);
                transport.open();
                TProtocol protocol = new TBinaryProtocol(transport);
                return new MessageService.Client(protocol);
            }catch (Exception e) {
                tryTime++;
                if (tryTime == 5) {
                    return null;
                }
                //Log.e(TAG, "Cannot connect server", e);
            }
            try {
                Thread.sleep(100);
            }catch (InterruptedException e){
                Log.e(TAG, "Interrupted Exception", e);
            }
        }

    }

}
