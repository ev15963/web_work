<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    <%@ page import="java.io.*, java.util.Date" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>BoardList.jsp</title>
</head>
<body>
<h2>글 파일 뿌리기</h2>

<%
	request.setCharacterEncoding("UTF-8");

	String writer=request.getParameter("WRITER");
	String title=request.getParameter("TITLE");
	String content=request.getParameter("CONTENT");
	
	Date date = new Date();
	Long time=date.getTime();
	try {
		
	} catch(IOException ioe) {
		out.print("파일에 데이터를 쓸 수 없습니다.");
	} finally {
		try {
			pwriter.close();
		} catch(Exception e) {
			out.print("객체를 닫을 수 없습니다.");
		}
	}
	%>
	
</body>
</html>