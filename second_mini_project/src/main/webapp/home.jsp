<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>Dream Jeju</title>
<link rel="stylesheet" href="css/style.css">
</head>
<body>
	<div id="container">
		<header>
			<div id="logo">
				<a href="index.html">
					<h1>Dream Jeju</h1>
				</a>
			</div>
			<nav>
				<ul id="topInfo">
					<li><button><a href="index.jsp">�α׾ƿ�</a></button></li>
					<li>�ȳ��ϼ��� ${loginMember.name} ȸ���� </li>
				</ul>
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
					<li><a href="member/mypage.html">����������</a>
					<li><a href="member/managerPage.html">������������</a>
				</ul>
			</nav>
		</header>
		<div id="slideShow">
			<div id="slides">
				<img src="images/photo-1.jpg" alt=""> <img
					src="images/photo-2.jpg" alt=""> <img
					src="images/photo-3.jpg" alt="">
			</div>
		</div>
		<div id="contents">
			<div id="tabMenu">
				<input type="radio" id="tab1" name="tabs" checked> <label
					for="tab1">��������</label> <input type="radio" id="tab2" name="tabs">
				<label for="tab2">������</label>

				<div id="notice" class="tabContent">
					<h2>�������� �����Դϴ�.</h2>
					<ul>
						<li>�繫���� �����߽��ϴ�.</li>
						<li>[���� ����] ī�� ü�迡 �ʴ��մϴ�.</li>
						<li>[���� ����] ���� ���� �Ⱓ, ���� ü����� �����մϴ�.</li>
						<li>�ܿ�, ��õ ������</li>
						<li>����, ��õ ������</li>
					</ul>
				</div>
				<div id="gallery" class="tabContent">
					<h2>������ �����Դϴ�.</h2>
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
							<p>��ȭ����</p>
					</a></li>
					<li><a href="#"> <span id="quick-icon2"></span>
							<p>���� ��ũ��</p>
					</a></li>
					<li><a href="#"> <span id="quick-icon3"></span>
							<p>�����ϱ�</p>
					</a></li>

				</ul>
			</div>
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
						<li><a href="#"><img src="images/sns-1.png"></a></li>
						<li><a href="#"><img src="images/sns-2.png"></a></li>
						<li><a href="#"><img src="images/sns-3.png"></a></li>
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