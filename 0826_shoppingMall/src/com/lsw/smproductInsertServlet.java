package com.lsw;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class smproductInsertServlet
 */
@WebServlet("/smproductInsertServlet")
public class smproductInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public smproductInsertServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		smproductDAO smDAO = new smproductDAO();
		shoppingMallVO smvo = new shoppingMallVO();
		List<shoppingMallVO> smproduct = smDAO.insertsmproduct(smvo.getProName()); //select sql 꺼내옴
		System.out.println("ㅇㅇㅇ"+smproduct);
		request.setAttribute("smproductList", smproduct);
		RequestDispatcher disp = request.getRequestDispatcher("smproductList.jsp");
		disp.forward(request, response); //요청한걸 응답
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
