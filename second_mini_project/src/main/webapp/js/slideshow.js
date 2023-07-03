var current = 0; // 이미지 위치를 나타내는 변수
showSlides(); // 기본 첫 이미지를 보여줌

function showSlides() {
	var slides = document.querySelectorAll("#slides > img");
	for(let i =0; i< slides.length; i++) {
		slides[i].style.display = "none"; // 모든 이미지를 감춤
	}
	current++; // 다음 이미지로 이동
	if(current > slides.length) //마지막 이미지로 이동
		current = 1; // 1번째로 이동
	slides[current -1].style.display = "block"; // 현재 위치 이미지 표시
	setTimeout(showSlides, 2000); // 2초마다 showSlides() 함수를 반복 실행  
}

/*
var slides = document.querySelectorAll("#slides > img");
var prev = document.getElementById("prev");
var next = document.getElementById("next");
var current = 0;

showSlides(current);
prev.onclick = prevSlide;
next.onclick = nextSlide;

function showSlides(n) {
  for (let i = 0; i < slides.length; i++) {
    slides[i].style.display = "none";
  }
  slides[n].style.display = "block";
}

function prevSlide() {
  if (current > 0) current -= 1;
  else
    current = slides.length - 1;
    showSlides(current);
}

function nextSlide() {
  if (current < slides.length - 1) current += 1;
  else
    current = 0;
    showSlides(current);  
}*/