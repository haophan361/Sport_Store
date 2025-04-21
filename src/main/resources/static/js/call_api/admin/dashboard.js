//dashboard.js
$(document).ready(function() {
    const $fetchStatsButton = $('#fetchStats');
    const $startDateInput = $('#startDate');
    const $endDateInput = $('#endDate');

    let revenueCostChart = null;
    let productQuantitiesChart = null;

    // Initialize charts
    const revenueCostCtx = document.getElementById('revenueCostChart').getContext('2d');
    const productQuantitiesCtx = document.getElementById('productQuantitiesChart').getContext('2d');

    // Function to update Revenue vs Cost chart
    function updateRevenueCostChart(revenueData, costData) {
        const labels = Object.keys(revenueData).sort();
        const revenueValues = labels.map(label => revenueData[label] || 0);
        const costValues = labels.map(label => costData[label] || 0);

        if (revenueCostChart) revenueCostChart.destroy();

        revenueCostChart = new Chart(revenueCostCtx, {
            type: 'line',
            data: {
                labels: labels,
                datasets: [
                    {
                        label: 'Doanh Thu',
                        data: revenueValues,
                        borderColor: '#4e73df',
                        fill: false
                    },
                    {
                        label: 'Chi Phí',
                        data: costValues,
                        borderColor: '#e74a3b',
                        fill: false
                    }
                ]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    }

    // Function to update Product Quantities chart
    function updateProductQuantitiesChart(productData) {
        const labels = Object.keys(productData);
        const quantities = Object.values(productData);

        if (productQuantitiesChart) productQuantitiesChart.destroy();

        productQuantitiesChart = new Chart(productQuantitiesCtx, {
            type: 'bar',
            data: {
                labels: labels,
                datasets: [{
                    label: 'Số Lượng Mua',
                    data: quantities,
                    backgroundColor: 'rgba(78, 115, 223, 0.2)',
                    borderColor: 'rgba(78, 115, 223, 1)',
                    borderWidth: 1
                }]
            },
            options: {
                responsive: true,
                scales: {
                    y: { beginAtZero: true }
                }
            }
        });
    }

    // Function to validate date range
    function isValidDateRange(startDate, endDate) {
        if (!startDate || !endDate) {
            bootbox.alert({
                title: "Thông báo",
                message: "Vui lòng chọn cả ngày bắt đầu và ngày kết thúc",
                backdrop: true
            });
            return false;
        }

        const start = new Date(startDate);
        const end = new Date(endDate);

        if (start > end) {
            bootbox.alert({
                title: "Thông báo",
                message: "Ngày bắt đầu phải trước ngày kết thúc",
                backdrop: true
            });
            return false;
        }

        return true;
    }

    // Load all data on page load
    function loadAllStatistics() {
        apiRequest(
            '/admin/all-revenue',
            'GET',
            { 'Content-Type': 'application/json' },
            null,
            null,
            null,
            'include',
            function(revenueData) {
                apiRequest(
                    '/admin/all-cost',
                    'GET',
                    { 'Content-Type': 'application/json' },
                    null,
                    null,
                    null,
                    'include',
                    function(costData) {
                        updateRevenueCostChart(revenueData, costData);
                    }
                );
            }
        );

        apiRequest(
            '/admin/all-product-quantities',
            'GET',
            { 'Content-Type': 'application/json' },
            null,
            null,
            null,
            'include',
            function(productData) {
                updateProductQuantitiesChart(productData);
            }
        );
    }

    // Load statistics for selected date range
    $fetchStatsButton.on('click', function() {
        const startDate = $startDateInput.val();
        const endDate = $endDateInput.val();

        if (!isValidDateRange(startDate, endDate)) {
            return;
        }

        apiRequest(
            `/admin/revenue?start=${startDate}&end=${endDate}`,
            'GET',
            { 'Content-Type': 'application/json' },
            null,
            null,
            null,
            'include',
            function(revenueData) {
                apiRequest(
                    `/admin/cost?start=${startDate}&end=${endDate}`,
                    'GET',
                    { 'Content-Type': 'application/json' },
                    null,
                    null,
                    null,
                    'include',
                    function(costData) {
                        updateRevenueCostChart(revenueData, costData);
                    }
                );
            }
        );

        apiRequest(
            `/admin/product-quantities?start=${startDate}&end=${endDate}`,
            'GET',
            { 'Content-Type': 'application/json' },
            null,
            null,
            null,
            'include',
            function(productData) {
                updateProductQuantitiesChart(productData);
            }
        );
    });

    // Load charts when page loads
    loadAllStatistics();
});