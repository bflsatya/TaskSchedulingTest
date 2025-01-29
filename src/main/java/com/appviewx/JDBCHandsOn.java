package com.appviewx;



import com.appviewx.controllers.AddTenantController;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
/*import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;*/

import java.sql.*;
import java.util.Arrays;
import java.util.List;

public class JDBCHandsOn {


    private static final Logger LOGGER = LogManager.getLogger(JDBCHandsOn.class);
    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
        LOGGER.info("Started Logging");
        int i=0;
        while (true) {
            Thread.sleep(1000);
            LOGGER.info("Log line : {}" + i++);
        }

        /*List<String> vowels = Arrays.asList("a", "e", "i", "o", "u");
        StringBuilder result = vowels.stream().collect(StringBuilder::new, (x, y) -> x.append(y),
                (a, b) -> a.append(",").append(b));
        System.out.println(result.toString());

        StringBuilder result1 = vowels.parallelStream().collect(StringBuilder::new, (x, y) -> x.append(y),
                (a, b) -> a.append(",").append(b));
        System.out.println(result1.toString());

        String result2 = vowels.parallelStream()
                .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                .toString();
        System.out.println(result2);

        System.out.println(getEscapedString("test,te,&,^"));


        Class.forName("com.mysql.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testDB", "satya", "password");


        String selectAllQuery = "select * from pet where name = ?";
        PreparedStatement preparedStatement = connection.prepareStatement(selectAllQuery);
        preparedStatement.setString(1,"Claws");
        ResultSet resultSet = preparedStatement.executeQuery();
        while(resultSet.next()) {
            String name = resultSet.getString(1);
            System.out.println("name is " + name);
        }*/
    }

    public static String getEscapedString(String name) {
        return name.chars().map(i -> ((i >= 97 && i <= 122) || (i >= 64 && i <= 90) || (i >= 48 && i <= 57) || i == 46 || i == 47) ? (char) i : '-')
                .collect(StringBuilder::new, (str, i) -> str.append((char) i), StringBuilder::append).toString();
    }
}
