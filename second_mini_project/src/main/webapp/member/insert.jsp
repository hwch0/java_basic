<%@page import="member.Member"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.io.IOException"%>
<%@page import="java.io.BufferedOutputStream"%>
<%@page import="java.io.FileOutputStream"%>
<%@page import="java.io.ObjectOutputStream"%>
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
String name = request.getParameter("name");
String pwd = request.getParameter("pwd");
String pwd2 = request.getParameter("pwd2");
int age = Integer.parseInt(request.getParameter("age"));
String phone = request.getParameter("phone");
String address = request.getParameter("address");
String gender = request.getParameter("gender");
Boolean type = false;

String message = "";

String fileName = "c:\\Users\\KOSA\\Temp\\memberNew.db";
File file = new File(fileName);
ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
List<Member> memberList = (List<Member>) ois.readObject();

boolean result = memberList.stream().noneMatch(m -> m.getUid().equals(uid));
if (result) {
	memberList.add(new Member(uid, name, pwd, age, phone, address, gender, type));
	
	ObjectOutputStream oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
	oos.writeObject(memberList);
	oos.close();

	message = "회원 가입 성공";
	
} else {
	message = "아이디가 존재 합니다";
}

%>

<%=message %>
