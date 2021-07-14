function prevPage() {
    var form = document.getElementById('filter');
    var page = document.getElementById('page');
    page.value--;
    if (page.value >= 1) {
        form.submit();
    }
    else {
        page.value++;
    }
}

function nextPage() {
    var form = document.getElementById('filter');
    var page = document.getElementById('page');
    var max = document.getElementById('maxPage');
    page.value++;
    if (page.value <= max.value) {
        form.submit();
    }
    else {
        page.value--;
    }
}