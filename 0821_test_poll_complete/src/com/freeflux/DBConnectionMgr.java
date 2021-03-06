package com.freeflux;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

/** 데이터에 접근할 수 있는 객체 (DAO) 역할 담당**/
public class DBConnectionMgr {
	public Connection conn = null;
	public PreparedStatement pstmt = null;
	public Statement stmt = null;
	public ResultSet rs = null;

	private final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	private final String ORACLE_URL = "jdbc:oracle:thin:@127.0.0.1:1521:XE";

    public DBConnectionMgr() {
    }

    /** 기본 생성자 : 각 Servlet 에서 가장 먼저 객체 생성 시 **/
	public void connect() {
		try {
			Class.forName(ORACLE_DRIVER);
			System.out.println("==> 드라이버 로딩 성공!");

			this.conn = DriverManager.getConnection(ORACLE_URL, "lsw", "1234");
			System.out.println("==> DB접속성공!");

		} catch (ClassNotFoundException e) {
			System.out.println("DRIVER LOAD ERR => " + e.getMessage());

		} catch (SQLException e) {
			System.out.println("CONNECT ERR => " + e.getMessage());
		}
	}
	/** =========================================================== **/
	
	/**
	 * 설문의 가장 높은 num값 :
	 * 
	 * @return
	 */
	public int getMaxNum() {
		String sql = null;
		int maxNum = 0;
		try {
			this.connect();
			sql = "select max(num) from tblPollList";
			this.pstmt = this.conn.prepareStatement(sql);
			
			this.rs = this.pstmt.executeQuery();
			
			if (this.rs.next()) {
				maxNum = this.rs.getInt(1);	// 가장 높은 num값
			}
		} catch (Exception e) {
			System.out.println("getMaxNum() ERR => " + e.getMessage());
		}
		return maxNum;
	}	// getMaxNum() END
	
	
	/** =========================================================== **/
	
	/**
	 * 설문 등록 처리 : 
	 * 
	 * @param plBean, piBean
	 */
	public boolean insertPoll(PollListBean plBean, PollItemBean piBean) {
		boolean flag = false;  // 최종 성공 여부
		String sql = null;
		try {
			this.connect();
			this.pstmt = null;
			
			sql = "insert into tblPollList (num, question, sdate, edate, wdate, type) "
					+ "values (seqPollList.nextval, ?, ?, ?, sysdate, ?)";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setString(1, plBean.getQuestion());
			this.pstmt.setString(2, plBean.getSdate());
			this.pstmt.setString(3, plBean.getEdate());
			this.pstmt.setInt(4, plBean.getType());
			
			int result = this.pstmt.executeUpdate();
			
			this.pstmt = null;
			
			if (result == 1) {
				String[] item = piBean.getItem();

				int itemnum = this.getMaxNum();
				
				sql = "insert into tblPollItem (listnum, itemnum, item, count) "
										+ "values ("+itemnum+", ?, ?, 0)";
				this.pstmt = this.conn.prepareStatement(sql);

				int j = 0;
				for (int i = 0; i < item.length; i++) {
					if (item[i] == null || item[i].length()==0) {
						break;
					}

					this.pstmt.setInt(1, i);
					this.pstmt.setString(2, item[i]);

					j = this.pstmt.executeUpdate();
				}
				if (j > 0) {
					flag = true;
				}
			}
		} catch (SQLException e) {
			System.out.println("insertPoll() ERR => " + e.getMessage());
		}
		return flag;
	}
	
	/** =========================================================== **/
	
	/**
	 * 설문 목록 조회  : 
	 * 
	 * @return
	 */
	public List<PollListBean> getAllList() {
		String sql = null;
		List<PollListBean> pollList = new ArrayList<PollListBean>();
		try {
			this.connect();
			sql = "select * from tblPollList order by num desc";
			this.pstmt = this.conn.prepareStatement(sql);
			
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				PollListBean plBean = new PollListBean();
				
				plBean.setNum(this.rs.getInt("num"));
				plBean.setQuestion(this.rs.getString("question"));
				plBean.setSdate(this.rs.getString("sdate"));
				plBean.setEdate(this.rs.getString("edate"));
				
				pollList.add(plBean);
			}
		} catch (Exception e) {
			System.out.println("getAllList() ERR => " + e.getMessage());
		} 
		return pollList;
	}
	
	/** =========================================================== **/
	
	/**
	 * 설문 리스트 조회 : 
	 * 
	 * @param num
	 * @return
	 */
	public PollListBean getList(int num) {   // 설문 작성 후 : 0 / 설문리스트 선택 해당 num
		String sql = null;
		PollListBean plBean = new PollListBean();
		try {
			this.connect();
			if (num == 0) {
				sql = "select * from tblPollList order by num desc"; // 설문 작성 후 전체
			}else {
				sql = "select * from tblPollList where num=" + num; // 선택한 설문만.
			}
			this.pstmt = this.conn.prepareStatement(sql);
			this.rs =this.pstmt.executeQuery();
			if (this.rs.next()) {
				plBean.setQuestion(this.rs.getString("question")); // 설문내용
				plBean.setType(this.rs.getInt("type"));	 // 복수 선택여부 : checkbox / radio
				plBean.setActive(this.rs.getInt("active")); // 설문 가능 여부
			}
		} catch (Exception e) {
			System.out.println("getList() ERR => " + e.getMessage());
		}
		return plBean;
	}
	
	/** =========================================================== **/
	
	/**
	 * 설문 아이템  조회 : 
	 * 
	 * @param num
	 * @return
	 */
	public List<String> getItem(int num) {
		String sql = null;
		List<String> vlist = new ArrayList<String>();
		try {
			this.connect();
			if (num == 0) {
				num = getMaxNum();
			}
			sql = "select item from tblPollItem where listnum = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			this.pstmt.setInt(1, num);
			this.rs = this.pstmt.executeQuery();
			
			while (this.rs.next()) {
				vlist.add(this.rs.getString(1)); //또는 this.rs.getString("item")
			}
		} catch (Exception e) {
			System.out.println("getItem() ERR => " + e.getMessage());
		} 
		return vlist;
	}
	
	/** =========================================================== **/
	
	
	/**
	 * 설문 아이템  투표수 증가 : 
	 * 
	 * @param num, itemnum
	 * @return
	 */
	// "<input type=checkbox name='itemnum' value='"+i+"'>"
	public boolean updatePoll(int num, String[] itemnum) {
		boolean flag = false;
		String sql = null;
		try {
			this.connect();                                                                   //1                       1
			sql = "update tblPollItem set count = count+1 where listnum=? and itemnum = ?";
			this.pstmt = this.conn.prepareStatement(sql);
			if (num == 0) {
				num = this.getMaxNum();
			}
			
			for (int i = 0; i < itemnum.length; i++) {
				if (itemnum[i] == null || itemnum[i].equals("")) {
					break;
				}
				this.pstmt.setInt(1, num);
				this.pstmt.setInt(2, Integer.parseInt(itemnum[i]));
				int j = this.pstmt.executeUpdate();
				if (j > 0) {
					flag = true;
				}
			}
		} catch (Exception e) {
			System.out.println("updatePoll() ERR => " + e.getMessage());
		} 
		return flag;
	}
	
	/** =========================================================== **/
	
	/**
	 * 설문 아이템  조회 : 
	 * 
	 * @param num, itemnum
	 * @return
	 */
	public List<PollItemBean> getView(int num) {
		String sql = null;
		List<PollItemBean> vlist = new ArrayList<PollItemBean>();

		try {
			this.connect();
			sql = "select item, count from tblPollItem where listnum=?";
			this.pstmt = this.conn.prepareStatement(sql);
			
			if (num == 0) {
				this.pstmt.setInt(1, this.getMaxNum());
			}else {
				this.pstmt.setInt(1, num);
			}
			
			this.rs = this.pstmt.executeQuery();
			while (this.rs.next()) {
				PollItemBean piBean= new PollItemBean();
				
				String item[] = new String[1];
				item[0] = this.rs.getString(1);		// tblPollItem 테이블의 item 필드 값
				piBean.setItem(item);					// private String [] item; // 아이템 내용
				piBean.setCount(this.rs.getInt(2)); // tblPollItem 테이블의 count 필드 값
				
				vlist.add(piBean);
			}
		} catch (Exception e) {
			System.out.println("updatePoll() ERR => " + e.getMessage());
		} 
		return vlist;
	} // END
	
	
	
	/**
	 * 설문 count 합계 조회 : 
	 * 
	 * @param num, itemnum
	 * @return
	 */
	public int sumCount(int num) {

		String sql = null;
		int count = 0;
		try {
			this.connect();
			sql = "select sum(count) from tblPollItem where listnum=?";
			this.pstmt = this.conn.prepareStatement(sql);
			if (num == 0) {
				this.pstmt.setInt(1, this.getMaxNum());
			}else {
				this.pstmt.setInt(1, num);
			}
			
			this.rs = this.pstmt.executeQuery();
			if (this.rs.next()) {
				count = this.rs.getInt(1);
			}
		} catch (Exception e) {
			System.out.println("sumCount() ERR => " + e.getMessage());
		} 
		return count;
	}
	
	
	
	
	
	/**
	 * 사용이 끝난 자원 해제 : 각 Servlet 에서 가장 나중에 호출 
	 * 
	 * @param boardModel
	 */
	public void close() {
		try {
			if (this.rs != null) {
				this.rs.close();
			}
			if (this.stmt != null) {
				this.stmt.close();
			}
			if (this.pstmt != null) {
				this.pstmt.close();
			}
			if (this.conn != null) {
				this.conn.close();
			}
		} catch (SQLException e) {
			System.out.println("CLOSED ERR => " + e.getMessage());
		}
	}
}
