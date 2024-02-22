const allLoadings = document.querySelectorAll('.loading');

window.addEventListener('load', () => {
    setInterval(() => {
        allLoadings.forEach(item => {
            item.classList.remove('loading');
        });
    }, 500);
});

document.addEventListener("DOMContentLoaded", function() {
    var sendButton = document.getElementById('sendButton');
    var fileCheckboxes = document.getElementsByName('fileCheckbox');

    // 초기 상태에서는 버튼 비활성화
    sendButton.disabled = true;
    sendButton.classList.add('disabled'); // 버튼을 비활성화 상태로 스타일 적용

    // 체크박스의 변경 사항을 감지하여 버튼 활성화 또는 비활성화
    fileCheckboxes.forEach(function(checkbox) {
        checkbox.addEventListener('change', function() {
            var anyChecked = Array.from(fileCheckboxes).some(function(checkbox) {
                return checkbox.checked;
            });
            sendButton.disabled = !anyChecked;
            if (anyChecked) {
                sendButton.classList.remove('disabled'); // 버튼을 활성화 상태로 스타일 적용
            } else {
                sendButton.classList.add('disabled'); // 버튼을 비활성화 상태로 스타일 적용
            }
        });
    });
});

function updateButtonState() {
    var sendButton = document.getElementById('sendButton');
    var fileCheckboxes = document.getElementsByName('fileCheckbox');
    var anyChecked = Array.from(fileCheckboxes).some(function(checkbox) {
        return checkbox.checked;
    });
    sendButton.disabled = !anyChecked;
}

function toggleCardSelection(cardElement) {
    var checkbox = cardElement.querySelector('input[type=checkbox]');
    checkbox.checked = !checkbox.checked;

    // 카드 선택 상태 토글 후, 버튼 상태 업데이트를 위한 함수 호출
    updateButtonState(); // 이 함수는 체크박스 상태에 따라 버튼을 활성화/비활성화합니다.

    if (checkbox.checked) {
        cardElement.classList.add('card-selected');
    } else {
        cardElement.classList.remove('card-selected');
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
