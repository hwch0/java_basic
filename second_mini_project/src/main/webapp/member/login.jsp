<%@page import="member.Member"%>
<%@page import="java.util.Optional"%>
<%@page import="java.util.List"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.File"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%
String uid = request.getParameter("uid");
String pwd = request.getParameter("pwd");
String message = "";

String fileName = "c:\\Users\\KOSA\\Temp\\memberNew.db";
File file = new File(fileName);
ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
List<Member> memberList = (List<Member>) ois.readObject();

System.out.println(memberList);

Optional<Member> optionalMember = memberList.stream().filter(m -> m.getUid().equals(uid) && m.getPwd().equals(pwd)).findFirst();
if(optionalMember.isPresent()) {
	session.setAttribute("loginMember", optionalMember.get());
	message= "로그인 성공";
} else {
	message = "아이디 또는 비밀번호가 잘못되었습니다.";
}

request.setAttribute("message", message);
request.getRequestDispatcher("loginSucc.jsp").forward(request, response);
%>

<%-- 
<%=message%> <br/> <!-- message 값 출력 -->
${loginMember.name} 회원님 <br/>
<a href="../index.html">홈</a>"
 --%>
