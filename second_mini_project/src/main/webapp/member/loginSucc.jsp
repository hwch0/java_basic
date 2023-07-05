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
			<h1>Login Success</h1> <br/>
		    <p>${message}</p> <br/>
		    <p>${loginMember.name} 회원님</p> <br/>
		    <button id="home"><a href="../index.jsp" style="color: white">HOME</a></button>
		</div>
		
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
</body>
</html>
