import java.util.concurrent.*;
import org.apache.thrift.*;
import org.apache.thrift.transport.*;
import org.apache.thrift.protocol.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Collections;
import java.util.Hashtable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.sql.*;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;


public class MessageHandler implements MessageService.Iface {
	static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	static final String DB_URL = "jdbc:mysql://localhost/message_service";
	static final String USER = "root";
	static final String PASS = "1";
	//private Connection conn;
	//private Statement stmt;
	boolean firstRun;
	List<ConcurrentHashMap<String, List<String>>> mapArrayList;


	public MessageHandler(){
        	//conn = null;
		//stmt = null;
		//firstRun = true;
		mapArrayList = new ArrayList<ConcurrentHashMap<String, List<String>>>();
		for (int i = 0; i < 4; i++){
			mapArrayList.add(new ConcurrentHashMap<String, List<String>>());
		}		
	}

	@Override
       	public void joinX(String userId, String groupId) throws org.apache.thrift.TException{
		int groupIndex = determineGroupIndex(groupId);
		mapArrayList.get(groupIndex).put(userId, new ArrayList<String>());
		System.out.println("Join Succeed \n the user in the group is ");
		for (String userInMap : mapArrayList.get(groupIndex).keySet()){
			System.out.println(userInMap);
		}	
	}

        @Override
        public void quitX(String userId, String groupId) throws org.apache.thrift.TException{
                int groupIndex = determineGroupIndex(groupId);
                mapArrayList.get(groupIndex).remove(userId);
		System.out.println("Quit Succeed \n the user in the group is ");
                for (String userInMap : mapArrayList.get(groupIndex).keySet()){
                        System.out.println(userInMap);
        	}
	}
	

        @Override
        public List<String> transmitMessage(String userId, String groupId, String message) throws org.apache.thrift.TException{
		int groupIndex = determineGroupIndex(groupId);
                if (!mapArrayList.get(groupIndex).containsKey(userId)) {
			String errorMessage = "Wrongxyz";
			List<String> returnMess = new ArrayList<String>();
			returnMess.add(errorMessage);
			System.out.println("this user has not joined");
			return returnMess;
		}
		else {
			for (String keyUser : mapArrayList.get(groupIndex).keySet()){
				if (keyUser.equals(userId)){
					continue;
				}
				List<String> messListForEachUser = mapArrayList.get(groupIndex).get(keyUser);
				messListForEachUser.add(userId);
				messListForEachUser.add(message);
				mapArrayList.get(groupIndex).put(keyUser, messListForEachUser);
			}
			System.out.println("Message put successfully");
		}
		List<String> returnMess = mapArrayList.get(groupIndex).get(userId);
		/*for test only*/
		/*returnMess.add("Lincoln");
		returnMess.add("I am hansome");
		returnMess.add("Frey");
		returnMess.add("I am more handsom than you");*/

		mapArrayList.get(groupIndex).put(userId, new ArrayList<String>());
		System.out.println("Message return successfully");
		return returnMess;
	}

	@Override
        public List<String> checkPulse(String userId, String groupId) throws org.apache.thrift.TException{
                int groupIndex = determineGroupIndex(groupId);
               	List<String> returnMess = mapArrayList.get(groupIndex).get(userId);
                mapArrayList.get(groupIndex).put(userId, new ArrayList<String>());
		/*for test only*/
                //returnMess.add("Server");
		//returnMess.add("receivePulse");

                return returnMess;
        }

	@Override
	public Set<String> checkUser(String groupId) throws org.apache.thrift.TException{
                int groupIndex = determineGroupIndex(groupId);
		System.out.println("Group User is fedback to user request");
		return mapArrayList.get(groupIndex).keySet();
        }

	private int determineGroupIndex(String groupId){
		int groupIndex = 0;
		switch (groupId) {
                        case "game":
                                groupIndex = 0;
			break;
			case "music":
				groupIndex = 1;
			break;
			case "color":
				groupIndex = 2;
			break;
			case "run":
				groupIndex = 3;
			break;
		}
		return groupIndex;
	}

	@Override
    	public void registerX(String userId, String password) throws org.apache.thrift.TException{
			Statement stmt = null;
			
			try {
                		Class.forName("com.mysql.jdbc.Driver").newInstance();
                		Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/message_service?" + "user=root&password=1");
                		stmt = conn.createStatement();
			
				String sql = "INSERT INTO users VALUES('" + userId + "','" + password + "')";
				System.out.println(sql);
	                        stmt.execute(sql);
                	}catch(SQLException ex){
				System.out.println("SQLException: " + ex.getMessage());
    				System.out.println("SQLState: " + ex.getSQLState());
    				System.out.println("VendorError: " + ex.getErrorCode());
			}
			catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
			catch(InstantiationException e) {
				e.printStackTrace();
			}
			catch(IllegalAccessException e) {
                                e.printStackTrace();
                        }
			finally{
				if (stmt != null) {
        			try {
            				stmt.close();
        			} catch (SQLException sqlEx) { } // ignore
	        			stmt = null;
				}
			}

	}

	@Override
        public String loginX(String userId) throws org.apache.thrift.TException{
	    	Statement stmt = null;
		ResultSet rs = null;
		String pass = "";
	    try{
		Class.forName("com.mysql.jdbc.Driver").newInstance();
	        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost/message_service?" + "user=root&password=1");
                stmt = conn.createStatement();
		
			String sql = "SELECT password from users where userId = '" + userId + "'";
			//String pass = "";
			System.out.println(sql);			
			rs = stmt.executeQuery(sql);
			while (rs.next()){
				pass = rs.getString("password");
			}
			System.out.println("a new user log in");
			System.out.println(pass);
			//return pass;
	    }
			catch(SQLException ex){
                                System.out.println("SQLException: " + ex.getMessage());
                                System.out.println("SQLState: " + ex.getSQLState());
                                System.out.println("VendorError: " + ex.getErrorCode());
                        }
                        catch(ClassNotFoundException e) {
                                e.printStackTrace();
                        }
                        catch(InstantiationException e) {
                                e.printStackTrace();
                        }
                        catch(IllegalAccessException e) {
                                e.printStackTrace();
                        }
                        finally{
                                if (stmt != null) {
                                try {
                                        stmt.close();
                                } catch (SQLException sqlEx) { } // ignore
                                        stmt = null;
                                }
                                if (rs != null) {
                                try {
                                        rs.close();
                                } catch (SQLException sqlEx) { } // ignore
                                        rs = null;
                                }
				//String emptyStr = "";
				return pass;
                        }


	}
		

}
