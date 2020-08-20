package com.freeflux.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.freeflux.dao.BoardDAO;
import com.freeflux.dto.BoardModel;
import com.freeflux.util.PageNavigator;

/**
 * Servlet implementation class BoardListServlet
 */
@WebServlet("/boardList")
public class BoardListServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
				// 파라미터
				String pageNum = request.getParameter("pageNum");  // 
				String searchType = request.getParameter("searchType");
				String searchText = request.getParameter("searchText");

				if (pageNum == null) {
					pageNum = "1";
				}
				if (searchText == null) {
					searchType = "";
					searchText = "";
				}
				
				// DTO
				BoardModel boardModel = new BoardModel();
				boardModel.setPageNum(pageNum);
				boardModel.setSearchType(searchType);
				boardModel.setSearchText(searchText);
				
				// DAO
				BoardDAO boardDAO = new BoardDAO();
				boardDAO.connect();
				
				// 게시물 총 수
				int totalCount = boardDAO.selectCount(boardModel);
System.out.println(">>>>> totalCount <<<<<"+totalCount);


				// 게시물 목록을 얻는 쿼리 실행
				List<BoardModel> boardList = boardDAO.selectList(boardModel);
				
				// View 사용될 객체 설정
				request.setAttribute("totalCount", totalCount);
				
				//
				PageNavigator pageNavigator=new PageNavigator();
				String pageNums = pageNavigator.getPageNavigator(totalCount, 
																					  boardModel.getListCount(), 
																					  boardModel.getPagePerBlock(), 
																					  Integer.parseInt(pageNum), 
																					  searchType, 
																					  searchText);
System.out.println(">>>>> pageNums <<<<<"+pageNums);			
				request.setCharacterEncoding("UTF-8");
				request.setAttribute("PAGE_NAVIGATOR", pageNums);
				request.setAttribute("LIST", boardList);
				request.setAttribute("MODEL", boardModel);
				
				response.setCharacterEncoding("UTF-8");
				// View 보내기
				RequestDispatcher requestDispatcher = request.getRequestDispatcher("boardList.jsp");
				requestDispatcher.include(request, response);
	}

}	// BoardListServlet class END





