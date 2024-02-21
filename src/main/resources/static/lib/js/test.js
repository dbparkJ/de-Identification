function DropFile(dropAreaId, fileListId) {
    let dropArea = document.getElementById(dropAreaId);
    let fileList = document.getElementById(fileListId);

    function preventDefaults(e) {
        e.preventDefault();
        e.stopPropagation();
    }

    function highlight(e) {
        preventDefaults(e);
        dropArea.classList.add("highlight");
    }

    function unhighlight(e) {
        preventDefaults(e);
        dropArea.classList.remove("highlight");
    }

    function handleDrop(e) {
        unhighlight(e);
        let dt = e.dataTransfer;
        let files = dt.files;

        handleFiles(files);

        const fileList = document.getElementById(fileListId);
        if (fileList) {
            fileList.scrollTo({ top: fileList.scrollHeight });
        }
    }

    function handleFiles(files) {
        files = [...files];
        files.forEach(file => {
            previewFile(file);
            uploadFile(file); // 파일을 서버로 업로드합니다.
        });
    }

    function uploadFile(file) {
        let formData = new FormData();
        formData.append('file', file); // 'file'은 서버에서 해당 파일 데이터를 식별하는 파라미터 이름입니다.
        

        fetch('/uploadFile', { // '/upload'는 파일을 처리할 서버의 엔드포인트입니다.
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                console.log('Success:', data);
                // 업로드 성공 시의 처리 로직을 여기에 추가할 수 있습니다.
            })
            .catch(error => {
                console.error('Error:', error);
                // 업로드 실패 시의 처리 로직을 여기에 추가할 수 있습니다.
            });
    }


    function previewFile(file) {
        console.log(file);
        fileList.appendChild(renderFile(file));
    }

    function formatFileSize(bytes, decimalPoints = 2) {
        if (bytes === 0) return '0 Bytes';

        const k = 1024;
        const dm = decimalPoints < 0 ? 0 : decimalPoints;
        const sizes = ['Bytes', 'KB', 'MB', 'GB', 'TB', 'PB', 'EB', 'ZB', 'YB'];

        const i = Math.floor(Math.log(bytes) / Math.log(k));

        return parseFloat((bytes / Math.pow(k, i)).toFixed(dm)) + ' ' + sizes[i];
    }



    function renderFile(file) {
        let fileDOM = document.createElement("div");
        fileDOM.className = "file";
        fileDOM.innerHTML = `
      <div class="thumbnail">
        <img src="https://img.icons8.com/pastel-glyph/2x/image-file.png" alt="파일타입 이미지" class="image">
      </div>
      <div class="details">
        <header class="header">
          <span class="name">${file.name}</span>
          <span class="size">${formatFileSize(file.size)}</span>
        </header>
        <div class="progress">
          <div class="bar"></div>
        </div>
        <div class="status">
          <span class="percent">100% done</span>
<!--          <span class="speed">90KB/sec</span>-->
        </div>
      </div>
    `;
        return fileDOM;
    }

    dropArea.addEventListener("dragenter", highlight, false);
    dropArea.addEventListener("dragover", highlight, false);
    dropArea.addEventListener("dragleave", unhighlight, false);
    dropArea.addEventListener("drop", handleDrop, false);

    return {
        handleFiles
    };
}

const dropFile = new DropFile("drop-file", "files");


