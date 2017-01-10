import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import org.apache.thrift.server.*;
import org.apache.thrift.server.TServer.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;
import org.apache.thrift.*;

import org.apache.log4j.*;

public class StorageNode {
  static Logger log;

  public static void main(String [] args) throws Exception {

      //initialize log4j
      BasicConfigurator.configure();
      log = Logger.getLogger(StorageNode.class.getName());

      // Launch a Thrift server
      
      int port = 8888;
      MessageService.Processor<MessageHandler> processor = new MessageService.Processor<>(new MessageHandler());
      TNonblockingServerSocket socket = new TNonblockingServerSocket(port);
      THsHaServer.Args sargs = new THsHaServer.Args(socket);
      sargs.protocolFactory(new TBinaryProtocol.Factory());
      sargs.transportFactory(new TFramedTransport.Factory());
      sargs.processorFactory(new TProcessorFactory(processor));
      sargs.maxWorkerThreads(16);
      TServer server = new THsHaServer(sargs);
      server.serve();	
      System.out.println("server is opened");
      }   
}
