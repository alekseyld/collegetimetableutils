package alekseyld.collegetimetableutils.interbase;

/**
 * Created by Alekseyld on 26.05.2018.
 */
public class InterBase {

    //    DataSource dataSource = new interbase.interclient.DataSource ();
//
//    // Set the standard properties
//    //dataSource.setServerName ("BASE.GDB");//localhost
//        dataSource.setDatabaseName ("localhost:C:/Program Files/Borland/INTRBASE/BASE.GDB");
////        dataSource.setDatabaseName ("81.30.211.85:C:/АРМ/Base.gdb");
////        dataSource.setDatabaseName ("81.30.211.85:C:/APM/Base.gdb");
//        dataSource.setDataSourceName ("C:/Program Files/Borland/INTRBASE/BASE.GDB");
//        dataSource.setDescription ("An example database of employees");
//        dataSource.setPortNumber (3050);
//        dataSource.setNetworkProtocol ("jdbc:interbase:");
//        dataSource.setRoleName (null);
//
//    // Set the non-standard properties
//        dataSource.setCharSet (interbase.interclient.CharacterEncodings.NONE);
//        dataSource.setSuggestedCachePages (0);
//        dataSource.setSweepOnConnect (false);
//
//    // Connect to the InterClient DataSource
//    java.sql.Connection c = null;
//    ServerManager serverManager = null;
//
//        try {
//        dataSource.setLoginTimeout (100);
//
//        c = dataSource.getConnection ("SYSDBA", "masterkey");
////            c = dataSource.getConnection ("ALEKSEYLD", "masterkey");
//
//        System.out.println ("got connection");
//
//    }
//        catch (java.sql.SQLException e) {
//        System.out.println ("sql exception: " + e.getMessage ());
//    }
//
//        try {
//        PreparedStatement preparedStatement = c.prepareStatement("SELECT * FROM STUDENTS");
//        preparedStatement.execute();
//
////            for(String s: strings) {
////                System.out.println (strings);
////            }
//
//    } catch (SQLException e) {
//        showSQLException(e);
//    }

//        try {
//            Statement s = c.createStatement();
//            ResultSet rs = s.executeQuery("SELECT * FROM STUDENTS");
//
//            java.sql.ResultSetMetaData rsMetaData = rs.getMetaData ();
//            System.out.println ("The query executed has " +
//                    rsMetaData.getColumnCount () +
//                    " result columns.");
//            System.out.println ("Here are the columns: ");
//            for (int i = 1; i <= rsMetaData.getColumnCount (); i++) {
//                System.out.println (rsMetaData.getColumnName (i) +
//                        " of type " +
//                        rsMetaData.getColumnTypeName (i));
//            }
//
//        }
//        catch (java.sql.SQLException e) {
//            showSQLException(e);
//        }

//        try {
//            c.close();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

//    private static void showSQLException (java.sql.SQLException e) {
//        // Notice that a SQLException is actually a chain of SQLExceptions,
//        // let's not forget to print all of them...
//        java.sql.SQLException next = e;
//        while (next != null) {
//            System.out.println (next.getMessage ());
//            System.out.println ("Error Code: " + next.getErrorCode ());
//            System.out.println ("SQL State: " + next.getSQLState ());
//            next = next.getNextException ();
//        }
//    }


}
