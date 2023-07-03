<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
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
				<a href="../home.jsp">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topMenu">
					<li><a href="#">��ü ����<span>��</span></a>
						<ul>
							<li><a href="#">ȸ�� ����</a></li>
							<li><a href="#">���� ����</a></li>
						</ul></li>
					<li><a href="#">���� ����<span>��</span></a>
						<ul>
							<li><a href="#">4.3 ��ȭ ����</a></li>
							<li><a href="#">���ڿ� ü��</a></li>
							<li><a href="#">���� ��ũ��</a></li>
						</ul></li>
					<li><a href="loginForm.html">�α���</a>
					<li><a href="insertMember.html">ȸ������</a>
				</ul>
			</nav>
		</header>
		<div id="formList">
			<h1>Login Success</h1> <br/>
		    <p>${message}</p> <br/>
		    <p>${loginMember.name} ȸ����</p> <br/>
		    <button id="home"><a href="../index.jsp" style="color: white">HOME</a></button>
		</div>
		
		<footer>
			<div id="bottomMenu">
				<ul>
					<li><a href="#">ȸ��Ұ�</a></li>
					<li><a href="#">��������ó����ħ</a></li>
					<li><a href="#">������</a></li>
					<li><a href="#">����Ʈ��</a></li>
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
			<p>����Ư����ġ�� ***�� ***�� *** (��ǥ��ȭ) 123-456-7890</p>
			</div>


		</footer>
	</div>

	<script src="js/slideshow.js"></script>
</body>
</html>
