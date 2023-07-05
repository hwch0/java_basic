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
		
		
			<div id="findMenu">
				<input type="radio" id="tab1" name="tabs" onclick="view()" checked> 
				<label for="tab1">아이디 찾기</label> 
				<input type="radio" id="tab2" onclick="view()" name="tabs">
				<label for="tab2">비밀번호 찾기</label>

				<div id="findId" class="findContent">
				
				<div id="formList" class="findContent">
				<form action="find.jsp" method="post" autocomplete="off" >
					<label for="name">이름 : </label>
					<input id="name" type="text" name="name"/><br/>
					<label for="phone">전화번호 : </label>
					<input id="phone" type="text" name="phone"/><br/>
					<input id="findType" name="findType" value="findId" style="visibility:hidden">
					<button type="submit">아이디 찾기</button>
				</form>
				</div>
				
				</div>
				<div id="findPwd" class="findContent">
				
				<div id="formList" class="findContent">
				<form action="find.jsp" method="post" autocomplete="off" >
					<label for="uid">아이디 : </label>
					<input id="uid" type="text" name="uid"/><br/>
					<label for="phone">전화번호 : </label>
					<input id="phone" type="text" name="phone"/><br/>
					<input id="findType" name="findType" value="findPwd" style="visibility:hidden">
					<button type="submit">비밀번호 찾기</button>
				</form>
				</div>

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
	function view() {
		  var tab1 = document.getElementById("tab1");
		  var tab2 = document.getElementById("tab2");
		  var findId = document.getElementById("findId");
		  var findPwd = document.getElementById("findPwd");
		  
		  if (tab1.checked) {
			  console.log("tab1");
		    findId.style.display = "block";
		    findPwd.style.display = "none";
		  } else if (tab2.checked) {
			  console.log("tab2");
		    findId.style.display = "none";
		    findPwd.style.display = "block";
		  }
		}
	</script>
	
</body>
</html>
