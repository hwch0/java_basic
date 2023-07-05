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
		%>
		<header>
			<div id="logo">
				<a href="../index.jsp">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topInfo">
					<!-- 					<li><button><a href="index.jsp">로그아웃</a></button></li> -->
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
					<li><a href="managerPage.jsp">관리자페이지</a>
				</ul>
			</nav>
		</header>
		
		<div id="myPage">
			<div id="myPageMenu">
				<ul>
					<li><a href="modifyInfo.jsp"> <span id="myPage-icon1"></span>
							<p>정보수정</p>
					</a></li>
					<li><a href="removeMember.jsp"> <span id="myPage-icon2"></span>
							<p>회원탈퇴</p>
					</a></li>

				</ul>
			</div>
		</div>

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
	</script>
</body>
</html>