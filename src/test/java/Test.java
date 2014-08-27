
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.miracle.base.DBPool;
import com.miracle.tool.DownPic;

public class Test {

    private static Logger log = LoggerFactory.getLogger(Test.class);

    public static void main(String[] args) {
        DBPool.setConfig("E:\\WorkSpace\\secret\\config\\jdbc.properties");

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        String sql = "select id,secret from secret";
        log.debug("sql : " + sql);
        try {
            conn = DBPool.getInstance().getConnection("secret");
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                DownPic.download(rs.getString("secret"), "D:\\temp\\secret\\" + rs.getInt("id") + ".jpg");
                System.out.println(rs.getInt("id"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
            log.error("~~~~~~~~~~~~~~~ query secret error ! ", e);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DBPool.close(pstmt, rs, conn);
        }

    }
}
