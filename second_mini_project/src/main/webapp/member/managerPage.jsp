<%@page import="java.io.FileInputStream"%>
<%@page import="java.io.BufferedInputStream"%>
<%@page import="java.util.List"%>
<%@page import="java.io.ObjectInputStream"%>
<%@page import="java.io.File"%>
<%@page import="member.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");
%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Dream Jeju</title>
<link rel="stylesheet" href="../css/style.css">
</head>
<body>
	<div id="container">

		<%
		Member loginMember = (Member) session.getAttribute("loginMember");

		if (loginMember == null) {
		%>
		<header>
			<div id="logo">
				<a href="../index.jsp">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topMenu">
					<li><a href="#">단체 여행<span>▼</span></a>
						<ul>
							<li><a href="#">회사 연수</a></li>
							<li><a href="#">수학 여행</a></li>
						</ul></li>
					<li><a href="#">맞춤 여행<span>▼</span></a>
						<ul>
							<li><a href="#">4.3 평화 기행</a></li>
							<li><a href="#">곶자왈 체험</a></li>
							<li><a href="#">힐링 워크숍</a></li>
						</ul></li>
					<li><a href="loginForm.html">로그인</a>
					<li><a href="insertMember.html">회원가입</a>
				</ul>
			</nav>
		</header>

		<div id="formList">
			<h1>로그인 후 이용하세요</h1>
		</div>


		<%
		} else {
		System.out.println(loginMember.toString());
		boolean membertype = loginMember.getType();
		System.out.println(membertype);
		%>
		<header>
			<div id="logo">
				<a href="../index.jsp">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topInfo">
					<li><button onclick="logout()">로그아웃</button></li>
					<li>안녕하세요 ${loginMember.name} 회원님</li>
				</ul>
				<ul id="topMenu">
					<li><a href="#">단체 여행<span>▼</span></a>
						<ul>
							<li><a href="#">회사 연수</a></li>
							<li><a href="#">수학 여행</a></li>
						</ul></li>
					<li><a href="#">맞춤 여행<span>▼</span></a>
						<ul>
							<li><a href="#">4.3 평화 기행</a></li>
							<li><a href="#">곶자왈 체험</a></li>
							<li><a href="#">힐링 워크숍</a></li>
						</ul></li>
					<li><a href="mypage.jsp">마이페이지</a>
					<li><a href="managerPage.jsp" onclick="return checkManager()">관리자페이지</a>
					<input id="memberType" value="<%=membertype %>" style="visibility:hidden" />
				</ul>
			</nav>
		</header>
		
		 <table id="memberTable" style="display:none">
		 	<caption id="tableName">회원목록 전체 출력</caption>
		    <tr>
		      <th>UID</th>
		      <th>Name</th>
		      <th>Password</th>
		      <th>Age</th>
		      <th>Phone Number</th>
		      <th>Address</th>
		      <th>Gender</th>
		      <th>Member Type</th>
		    </tr>
		    <% 
		    
		    String fileName = "c:\\Users\\KOSA\\Temp\\memberNew.db";
		    File file = new File(fileName);
		    ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)));
		    List<Member> memberList = (List<Member>) ois.readObject();
		    
		    for (Member member : memberList) { %>
		    <tr>
		      <td><%= member.getUid() %></td>
		      <td><%= member.getName() %></td>
		      <td><%= member.getPwd() %></td>
		      <td><%= member.getAge() %></td>
		      <td><%= member.getPhoneNumber() %></td>
		      <td><%= member.getAddress() %></td>
		      <td><%= member.getGender() %></td>
		      <td><%= member.getType() %></td>
		    </tr>
		    <% } %>
		  </table>

		<%
		}
		%>

		<footer>
			<div id="bottomMenu">
				<ul>
					<li><a href="#">회사소개</a></li>
					<li><a href="#">개인정보처리방침</a></li>
					<li><a href="#">여행약관</a></li>
					<li><a href="#">사이트맵</a></li>
				</ul>
				<div id="sns">
					<ul>
						<li><a href="#"><img src="../images/sns-1.png"></a></li>
						<li><a href="#"><img src="../images/sns-2.png"></a></li>
						<li><a href="#"><img src="../images/sns-3.png"></a></li>
					</ul>
				</div>
			</div>
			<div id="company">
				<p>제주특별자치도 ***동 ***로 *** (대표전화) 123-456-7890</p>
			</div>


		</footer>
	</div>

	<script src="js/slideshow.js"></script>
	<script>
		function logout() {
			if (confirm("로그아웃 하시겠습니까?")) {
				window.location.href = "logout.jsp";
			}
		}
		
		function checkManager() {
			console.log("함수실행")
			var memberType = document.getElementById("memberType").value;
			var memberTable = document.getElementById("memberTable");
			console.log(memberType);
			if(memberType===true) {
				memberTable.style.display="block";
				return true;
			} else {
				alert("관리자만 접근 가능한 메뉴 입니다.");
				return false;
				
			}
		}
		
		window.onload = function(){
			var memberType = document.getElementById("memberType").value;
			var memberTable = document.getElementById("memberTable");
			console.log(memberType);
			if(memberType) {
				memberTable.style.display="block";
				return true;
			} else {
				alert("관리자만 접근 가능한 메뉴 입니다.");
				return false;
				
			}
	    }
		
		
	</script>
</body>
</html>