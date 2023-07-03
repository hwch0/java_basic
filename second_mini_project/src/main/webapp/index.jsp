<%@page import="member.Member"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%
request.setCharacterEncoding("UTF-8");%>


<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Dream Jeju</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<div id="container">

<% 
Member loginMember = (Member) session.getAttribute("loginMember");

if(loginMember==null) {

%>
		<header>
			<div id="logo">
				<a href="index.html">
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
					<li><a href="member/loginForm.html">로그인</a>
					<li><a href="member/insertMember.html">회원가입</a>
				</ul>
			</nav>
		</header>


<% }  else { 
		System.out.println(loginMember.toString());
%>
		<header>
			<div id="logo">
				<a href="index.html">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topInfo">
<!-- 					<li><button><a href="index.jsp">로그아웃</a></button></li> -->
					<li><button onclick="logout()">로그아웃</button></li>
					<li>안녕하세요 ${loginMember.name} 회원님 </li>
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
					<li><a href="member/mypage.html">마이페이지</a>
					<li><a href="member/managerPage.html">관리자페이지</a>
				</ul>
			</nav>
		</header>

<% } %>

		<div id="slideShow">
			<div id="slides">
				<img src="images/photo-1.jpg" alt=""> 
				<img src="images/photo-2.jpg" alt=""> 
				<img src="images/photo-3.jpg" alt="">
			</div>
		</div>
		<div id="contents">
			<div id="tabMenu">
				<input type="radio" id="tab1" name="tabs" checked> <label
					for="tab1">공지사항</label> <input type="radio" id="tab2" name="tabs">
				<label for="tab2">갤러리</label>

				<div id="notice" class="tabContent">
					<h2>공지사항 내용입니다.</h2>
					<ul>
						<li>사무실을 이전했습니다.</li>
						<li>[참가 모집] 카약 체험에 초대합니다.</li>
						<li>[참가 모집] 여름 방학 기간, 오름 체험단을 모집합니다.</li>
						<li>겨울, 추천 여행지</li>
						<li>가을, 추천 여행지</li>
					</ul>
				</div>
				<div id="gallery" class="tabContent">
					<h2>갤러리 내용입니다.</h2>
					<ul>
						<li><img src="images/img-1.jpg"></li>
						<li><img src="images/img-2.jpg"></li>
						<li><img src="images/img-3.jpg"></li>
						<li><img src="images/img-1.jpg"></li>
						<li><img src="images/img-2.jpg"></li>
						<li><img src="images/img-3.jpg"></li>
					</ul>
				</div>
			</div>

			<div id="links">
				<ul>
					<li><a href="#"> <span id="quick-icon1"></span>
							<p>평화기행</p>
					</a></li>
					<li><a href="#"> <span id="quick-icon2"></span>
							<p>힐링 워크숍</p>
					</a></li>
					<li><a href="#"> <span id="quick-icon3"></span>
							<p>문의하기</p>
					</a></li>

				</ul>
			</div>
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
						<li><a href="#"><img src="images/sns-1.png"></a></li>
						<li><a href="#"><img src="images/sns-2.png"></a></li>
						<li><a href="#"><img src="images/sns-3.png"></a></li>
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
		     window.location.href = "member/logout.jsp";
		  }
		}
	</script>
</body>
</html>