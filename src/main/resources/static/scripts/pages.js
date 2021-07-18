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

function setSort(sortField) {
    var form = document.getElementById('filter');
    var sort = document.getElementById('sort');
    var direction = document.getElementById('sortDirection');

    if (sort.value === sortField) {
    //    invert direction of sorting
        if (direction.value === 'ASC') {
            direction.value = 'DESC';
        }
        else {
            direction.value = 'ASC';
        }
    }
    else {
        sort.value = sortField;
        direction.value = 'ASC';
    }
    form.submit();
}