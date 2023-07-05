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
String findType = request.getParameter("findType");
String message = "";

String fileName = "c:\\Users\\KOSA\\Temp\\memberNew.db";
File file = new File(fileName);
ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
List<Member> memberList = (List<Member>) ois.readObject();

if(findType.equals("findId")) {
	String name = request.getParameter("name");
	String phone = request.getParameter("phone");
	Optional<Member> optionalMember = memberList.stream().filter(m -> m.getName().equals(name) && m.getPhoneNumber().equals(phone)).findFirst();
	if(optionalMember.isPresent()) {
		request.setAttribute("message", "아이디 찾기 결과");
		request.setAttribute("result", "찾으시는 아이디는 [" + optionalMember.get().getUid() +"] 입니다.");
	} else {
		request.setAttribute("message", "찾으시는 아이디가 없습니다.");
		request.setAttribute("result", "입력하신 정보를 확인해 주세요");
	}
	
} else if(findType.equals("findPwd")) {
	String uid = request.getParameter("uid");
	String phone = request.getParameter("phone");
	Optional<Member> optionalMember = memberList.stream().filter(m -> m.getUid().equals(uid) && m.getPhoneNumber().equals(phone)).findFirst();
	if(optionalMember.isPresent()) {
		request.setAttribute("message", "비밀번호 찾기 결과");
		request.setAttribute("result", "찾으시는 비밀번호는 [" + optionalMember.get().getPwd() +"] 입니다.");
	} else {
		request.setAttribute("message", "찾으시는 비밀번호가 없습니다.");
		request.setAttribute("result", "입력하신 정보를 확인해 주세요");
	}
}

request.getRequestDispatcher("findResult.jsp").forward(request, response);

%>