function selectFile(fileInfo) {
    const previewArea = document.getElementById('previewArea');
    previewArea.innerHTML = ''; // 기존 미리보기 내용을 초기화
    console.log(fileInfo)
    // fileInfo에서 파일 확장자 추출
    const extension = fileInfo.split('.').pop().toLowerCase();

    // 이미지 파일인 경우
    if (['jpg', 'jpeg', 'png'].includes(extension)) {
        const img = document.createElement('img');
        img.src = `/upload/de-Identification/server/files/${fileInfo}`;
        img.className = 'image'
        img.style.maxWidth = '100%'; // 미리보기 영역에 맞춰 크기 조절
        img.style.height = 'auto';
        previewArea.appendChild(img);
    }
    // 비디오 파일인 경우
    else if (['mp4', 'avi'].includes(extension)) {
        const video = document.createElement('video');
        video.controls = true;
        video.style.maxWidth = '100%'; // 미리보기 영역에 맞춰 크기 조절
        const source = document.createElement('source');
        source.src = `/upload/de-Identification/server/files/${fileInfo}`;
        source.type = `video/${extension}`;
        video.appendChild(source);
        previewArea.appendChild(video);
    }
}


function sendData() {
    var selectedValues = [];
    // 모든 체크박스를 순회하면서 체크된 것들의 값을 배열에 추가
    var checkboxes = document.querySelectorAll('input[type=checkbox]:checked');
    for (var i = 0; i < checkboxes.length; i++) {
        selectedValues.push(checkboxes[i].value);
    }

    // AJAX 요청을 사용하여 선택된 값을 서버로 전송
    var xhr = new XMLHttpRequest();
    xhr.open("POST", "http://111.111.111.26:5000/test", true); // YOUR_BACKEND_ENDPOINT를 백엔드 처리 URL로 변경
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.onreadystatechange = function() {
        if (xhr.readyState === 4 && xhr.status === 200) {
            console.log("Response from server: " + xhr.responseText); // 성공적으로 데이터를 보냈다는 응답 처리
        }
    };
    var data = JSON.stringify({"selectedValues": selectedValues});
    xhr.send(data);
}
