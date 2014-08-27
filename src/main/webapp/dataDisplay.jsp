<%@ page language="java" import="java.sql.*,java.util.*,com.miracle.base.*" pageEncoding="UTF-8"%>
<!-- 

@author hyliu
 用于数据展示。实现了排序、查询、分页
 只需要在页面提交数据库的表名即可
 
 -->
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

String tableName = request.getParameter("tableName");
if(tableName==null){
	tableName="secret";
}
String pNo = request.getParameter("pageNo");
int pageNo = 1;
    if(null != pNo && !(Integer.parseInt(pNo)<=0)){
      pageNo= Integer.parseInt(pNo);
    }
    int pageSize = 25;
    int pageTotal = 1; 
    String orderBy = request.getParameter("orderBy");
    if(orderBy==null||orderBy.equals("")){
    	orderBy="id";
    }
    String where = request.getParameter("where");
    String value = request.getParameter("value");
    String where2 = request.getParameter("where2");
    String value2 = request.getParameter("value2");
    
  %>
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title><%=tableName %></title>
    
  <meta http-equiv="pragma" content="no-cache">
  <meta http-equiv="cache-control" content="no-cache">
  <meta http-equiv="expires" content="0">    
  
  <link rel="stylesheet" type="text/css" href="css/style.css">
  </head>
  <body>
  <h3>
  <form action="dataDisplay.jsp">
  表名：
  <input type="text" name="tableName">
  <input type="submit" value="提交">
   表名：<%=tableName %>
  </form>
 
  </h3>
  <table border=1>
  <%
 
    String from = String.valueOf((pageNo-1)*pageSize);
   String pageNum= String.valueOf(pageSize);
  String sql = "select * from "+tableName+" limit "+from +","+pageNum;
  String totalsql = "select count(*) from "+tableName;
  
  String selectSQL = "select * from "+tableName;
  String whereSQL = " where "+where +"='"+value+"'";
  String orderSQL = " order by "+orderBy +" desc ";
  String whereSQL2 = " and "+where2 +"='"+value2+"'";
  String limitSQL = " limit "+from +","+pageNum;
  if( null != value2 && !value2.equals("") &&!value2.equals("null")){
    whereSQL = whereSQL + whereSQL2;
  }
  
   if( null != orderBy && !orderBy.equals("")){
     //sql= "select * from "+tableName+" order by "+orderBy+" limit "+from +","+pageNum;
     sql = selectSQL + orderSQL + limitSQL;
   }
   
   if( null != value && !value.equals("") &&!value.equals("null")){
    //sql = "select * from "+tableName+" where "+where +"='"+value+"'"+" limit "+from +","+pageNum;
    sql = selectSQL + whereSQL + limitSQL;
    //totalsql = "select count(*) from "+tableName+" where "+where +"='"+value+"'";
    totalsql = "select count(*) from "+tableName+whereSQL;
    if( null != orderBy && !orderBy.equals("")){
       //sql= "select * from "+tableName+" where "+where +"='"+value+"'"+" order by "+orderBy+" limit "+from +","+pageNum;
       sql = selectSQL + whereSQL + orderSQL + limitSQL;
     }
  }
	  Connection conn = null;
	  PreparedStatement pstmt =null;
	  ResultSet rs =null;
  
    try{
      conn=DBPool.getInstance().getConnection("secret");
      pstmt = conn.prepareStatement(sql);
      rs = pstmt.executeQuery();
      
      //获得各个列的名字
      ResultSetMetaData rsmd = rs.getMetaData();
      int colNum = rsmd.getColumnCount();
      List<String> colNames = new ArrayList<String>();
      
      for(int i = 1; i <= colNum; i++){
        String colname = rsmd.getColumnName(i);
        colNames.add(rsmd.getColumnName(i));
      }
      
      out.print("<tr><td colspan='"+colNum+"'>");
        %>
          <form action="dataDisplay.jsp" title="查询两个条件时用第二个条件">
            <select name="where">
            <%
            int timeNum = 0;
            for(String colname :colNames){
            %>
              <option value="<%=colname %>"><%=colname %></option>
            <% } %>
            </select>
            <input type="hidden" value="<%=tableName %>" name="tableName">
            <input type="text" value="" name="value">
            <select name="where2">
            <%
            for(String colname :colNames){
            %>
              <option value="<%=colname %>"><%=colname %></option>
            <% } %>
            </select>
            <input type="text" value="" name="value2">
            
            <input type="submit" value="查询">
             <%=where %> = <%=value %> | <%=where2 %> = <%=value2 %> |
             orderBy = <%=orderBy %>
            <a href="dataDisplay.jsp?tableName=<%=tableName %>">显示全部</a>
       </form>
        <%
      
      out.print("</td></tr>");
      out.print("<tr>");
      for(String colname :colNames){
        %>
        <td><a href="dataDisplay.jsp?pageNo=<%=pageNo %>&orderBy=<%=colname %>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>"><%=colname %></a></td>
      <%
      }
      out.print("</tr>");
        while(rs.next()){
          out.print("<tr>");
          %>
          <td><%=rs.getString("uid") %></td>
          <td><a href="<%=rs.getString("secret") %>"><img src="http://121.40.140.180/imageShare/Share?img=<%=rs.getString("secret") %>" width='80' height='80'></a></td>
          <td><%=rs.getString("comment") %></td>
          <td><%=rs.getInt("own") %></td>
          <td><%=rs.getTimestamp("create_dt") %></td>
          <td><%=rs.getString("md5") %></td>
          <td><%=rs.getInt("id") %><a href="<%=path%>/DelSecret?id=<%=rs.getInt("id")%>">删除</a></td>
          <%
          }
        out.print("</tr>");
        int all =0;
      pstmt = conn.prepareStatement(totalsql);
      rs = pstmt.executeQuery();
      if(rs.next()){
        all = rs.getInt(1); 
        pageTotal= (all%pageSize==0)?(all/pageSize):(all/pageSize+1);
      }
    }catch(Exception e){
      out.print(e);
      e.printStackTrace();
    }finally{
      DBPool.close(pstmt,rs,conn);
    }
  %>
  </table>
  <h2>
      
     <a href="<%=path %>/dataDisplay.jsp?pageNo=1&orderBy=<%=orderBy %>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>">首页</a>
    <a href="<%=path %>/dataDisplay.jsp?pageNo=<%=(pageNo-1 < 1)?pageNo:pageNo-1 %>&orderBy=<%=orderBy %>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>">上一页</a>
    第<%=pageNo %>页
    共<%=pageTotal %>页
    <a href="<%=path %>/dataDisplay.jsp?pageNo=<%=(pageNo>=pageTotal)?pageNo:pageNo+1 %>&orderBy=<%=orderBy %>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>">下一页</a>
    <a href="<%=path %>/dataDisplay.jsp?pageNo=<%=pageTotal %>&orderBy=<%=orderBy %>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>">尾页</a>
    第<input type="text" id="pageno" size="5">页 <input type="button" name="GO" value="GO" onclick="gopage();">
    </h2>
  </body>
  </html>
<script language="javascript">
function gopage(){
  var pageno = document.getElementById("pageno").value;//$("#pageno").val();
  if(pageno==""){
    alert("请输入页数");
  }  else{
    if(pageno > <%=pageTotal%> || pageno< 1){
      alert("请输入正确的页数");
    }else{
      self.location="dataDisplay.jsp?pageNo="+pageno+"&orderBy=<%=orderBy%>&where=<%=where %>&value=<%=value %>&where2=<%=where2 %>&value2=<%=value2 %>&tableName=<%=tableName%>";
    }
  }
}
</script>
