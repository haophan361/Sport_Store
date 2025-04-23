

function saveDiscount() {
    const form_Discount = {
        discount_percentage: document.getElementById("new_discount_percentage").value,
        start_date: document.getElementById("new_discount_start").value,
        end_date: document.getElementById("new_discount_end").value,
        active: document.getElementById("new_discount_active").checked ? 1 : 0
    }
    apiRequest("/admin/insert_discount", "POST", {'Content-type': 'application/json'}, JSON.stringify(form_Discount),
        null, null, "include", function () {
            $('#addDiscountModal').modal('hide')
            fetchDiscount()
        })
}

let selectedDiscountId = null;

function fetchDiscount() {
  fetch("/getAllDiscount")
    .then((response) => response.json())
    .then((data) => {
      const dropdown = document.getElementById("customDiscountDropdown");
      dropdown.innerHTML = "";

      const noDiscount = document.createElement("div");
      noDiscount.className = "dropdown-item text-muted";
      noDiscount.innerHTML = `<span>Không giảm giá</span>`;
      noDiscount.onclick = function () {
        const btn = noDiscount.closest(".dropdown").querySelector(".dropdown-toggle");
        btn.innerText = "Không giảm giá";
        selectedDiscountId = null;

        const hiddenInput = document.getElementById("discountId");
        if (hiddenInput) hiddenInput.value = "";
      };
      dropdown.appendChild(noDiscount);

      data.forEach((item) => {
        const div = document.createElement("div");
        div.className = "dropdown-item d-flex justify-content-between align-items-center";
        div.innerHTML = `
          <span>${item.discount_percentage}%</span>
          <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteDiscount(${item.discount_id})"></i>
        `;

        div.onclick = function (e) {
          if (e.target.tagName === "I") return;

          const btn = div.closest(".dropdown").querySelector(".dropdown-toggle");
          btn.innerText = `${item.discount_percentage}%`;

          selectedDiscountId = item.discount_id;

          const hiddenInput = document.getElementById("discountId");
          if (hiddenInput) hiddenInput.value = item.discount_id;
        };

        dropdown.appendChild(div);
      });


      const addNew = document.createElement("div");
      addNew.className = "dropdown-item text-primary";
      addNew.setAttribute("data-toggle", "modal");
      addNew.setAttribute("data-target", "#addDiscountModal");
      addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Giảm giá mới`;
      dropdown.appendChild(addNew);
    });
}

  
  function deleteDiscount(discountId) {
    if (confirm("Bạn có chắc muốn xóa giảm giá này?")) {
      fetch(`/admin/delete_discount/${discountId}`, { method: "DELETE" })
        .then((res) => {
          if (res.ok) fetchDiscount();
          else alert("Xoá thất bại");
        });
    }
  }
  