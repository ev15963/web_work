<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, com.freeflux.dto.*" %>
<%
	request.setCharacterEncoding("UTF-8");
	BoardModel searchModel = (BoardModel)request.getAttribute("MODEL");
	List<BoardModel> list=(ArrayList<BoardModel>)request.getAttribute("LIST");
	String pageNavigator=(String)request.getAttribute("PAGE_NAVIGATOR");  
	String leftPage = request.getParameter("LEFT_PAGE");
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8"></meta>
	<title>게시판 목록</title>

	<style type="text/css">
		* {font-size: 9pt;}
		p {width: 600px; text-align: right;}
		table thead tr th {background-color: gray;}
	</style>
</head>

<body>
 	<table width="1200" border="1" cellpadding="0" cellspacing="0"  align="center">   
	   <tr height="80" >    
		   <td colspan= "2"  align="center"> <!-- 상단 영역 --> 
		   		<jsp:include page= "top.jsp" flush= "false" />    
		   </td>   
	   </tr>  

<!-- 가운데 영역 시작 -->	   
	   <tr height="180">    
		   <td width= "150" valign= "top"  align="center"> <!-- 좌측 영역 --> 
		   		<jsp:include page= "sub3.jsp" flush= "false" />    
		   </td>  
		   
<!-- 본문 내용 시작 --> 		   
		<td width= "1020" valign= "top"  align="center"> 
		
<!-- 게시판 목록 출력 시작 -->
			<h3>게시판 목록 화면</h3>
			<hr>
				<!-- 검색부분 추가 시작 -->
				<form action="boardList" >
						<p>
							<select name="searchType">
								<option value="ALL" selected>전체검색</option>
								<option value="SUBJECT"  >제목</option>
								<option value="WRITER"  >작성자</option>
								<option value="CONTENTS"   >내용</option>
					
							</select>
							<input type="text" name="searchText"   required />
							<input type="submit" value="검색" />
						</p>
				</form>
				<!-- 검색부분 추가 끝 -->
				
				<table border="1" summary="게시판 목록"   width="1020" >
					<caption>게시판 목록</caption>
					<colgroup>
						<col width="50" />
						<col width="100" />
						<col width="80" />
						<!-- 등록 일자 컬럼 크기 추가 -->
						<col width="80" />
						<col width="70" />
					</colgroup>
					<thead>
						<tr>
							<th>번호</th>
							<th>제목</th>
							<th>작성자</th>
							<!-- 등록 일자 컬럼 추가 -->
							<th>작성일</th>
							<th>조회수</th>
						</tr>
					</thead>
			
					<tbody>
						<% if(list.size()==0){ %>
						<tr>
							<td align="center" colspan="5">등록된 게시물이 없습니다.</td>
						</tr>
						<% 
							}else{ 
								for(BoardModel model : list){
						%>
						<tr>
							<td align="center"><%= model.getNo() %></td>
							<td><a href="boardView?no=<%= model.getNo() %>" /><%= model.getSubject() %></a></td>
							<td align="center"><%= model.getWriter() %></td>
							<!-- 등록 일자 추가 -->
							<td align="center"><%= model.getModdate().substring(0, 10)%></td> 
							
							<td align="center"><%= model.getHit() %></td>
						</tr>
						<%
								}
							} 
						%>
					</tbody>
						
					<tfoot>
						<tr>
							<!-- 수정 -->
			<!-- 				<td align="center" colspan="5">Copyright ⓒ freeflux Corp. All Rights Reserved</td> -->
			
							<td align="center" colspan="5"><%= pageNavigator %></td>
						</tr>
					</tfoot>
				</table>
				
				<p>
					<input type="button" value="글쓰기"  onclick="location.href='boardWrite'" />
				</p>
<!-- 게시판 목록 출력 끝 -->

			</td>   
<!-- 본문 내용 종료 --> 	

			
	   </tr>   
<!-- 가운데 영역 끝 -->

<!-- 하단 영역 시작 --> 		   
	   <tr height="80" >    
		   <td colspan= "2"  align="center"> 
		   		<jsp:include page= "bottom.jsp" flush= "false" />    
		   </td>   
	   </tr> 
<!-- 하단 영역 끝 -->	   
	</table>
</body>
</html>






