document.addEventListener('DOMContentLoaded', function () {
    const currentUrl = new URL(window.location.href);

    const filterCheckboxes = document.querySelectorAll('.filter-checkbox');
    filterCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', function () {
            applyFilters();
        });
    });

    document.getElementById('searchButton').addEventListener('click', function () {
        const keyword = document.getElementById('searchKeyword').value;
        updateUrlParameter('keyword', keyword);
        updateUrlParameter('page', 1);
        window.location.href = currentUrl.toString();
    });

    document.getElementById('searchKeyword').addEventListener('keypress', function (e) {
        if (e.key === 'Enter') {
            document.getElementById('searchButton').click();
        }
    });

    const sortOptions = document.querySelectorAll('.sort-option');
    sortOptions.forEach(option => {
        option.addEventListener('click', function () {
            const sort = this.getAttribute('data-sort');
            updateUrlParameter('sort', sort);
            updateUrlParameter('page', 1);
            window.location.href = currentUrl.toString();
        });
    });

    const pageSizeOptions = document.querySelectorAll('.page-size-option');
    pageSizeOptions.forEach(option => {
        option.addEventListener('click', function () {
            const pageSize = this.getAttribute('data-size');
            updateUrlParameter('page_size', pageSize);
            updateUrlParameter('page', 1);
            window.location.href = currentUrl.toString();
        });
    });

    function applyFilters() {
        updateUrlParameter('page', 1);

        currentUrl.searchParams.delete('cost_segment');
        currentUrl.searchParams.delete('list_brand');
        currentUrl.searchParams.delete('list_option_size');

        document.querySelectorAll('input[name="cost_segment"]:checked').forEach(checkbox => {
            currentUrl.searchParams.append('cost_segment', checkbox.value);
        });

        document.querySelectorAll('input[name="list_brand"]:checked').forEach(checkbox => {
            currentUrl.searchParams.append('list_brand', checkbox.value);
        });

        document.querySelectorAll('input[name="list_option_size"]:checked').forEach(checkbox => {
            currentUrl.searchParams.append('list_option_size', checkbox.value);
        });

        window.location.href = currentUrl.toString();
    }

    function updateUrlParameter(param, value) {
        if (value) {
            currentUrl.searchParams.set(param, value);
        } else {
            currentUrl.searchParams.delete(param);
        }
    }
});