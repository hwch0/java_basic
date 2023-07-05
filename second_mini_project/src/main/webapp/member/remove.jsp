<%@page import="member.Member"%>
<%@page import="java.util.List"%>
<%@page import="java.io.File"%>
<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.ObjectOutputStream"%>
<%@page import="java.util.Optional"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
%>

<%
String uid = request.getParameter("uid");
String message = "";

String fileName = "c:\\Users\\KOSA\\Temp\\memberNew.db";
File file = new File(fileName);
ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
List<Member> memberList = (List<Member>) ois.readObject();

Optional<Member> optionalMember = memberList.stream().filter(m -> m.getUid().equals(uid)).findFirst();

if(optionalMember.isPresent()) {
	
	memberList.remove(optionalMember.get());
	
	ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	oos.writeObject(memberList);
	oos.close();
	
} else {
	request.setAttribute("message", "탈퇴 과정 중 문제가 생겼습니다.");
}

session.invalidate();
response.sendRedirect("../index.jsp");

%>