
function saveColor() {
    const color = document.getElementById("new_color_name").value
    apiRequest("/admin/insert_color", "POST", {}, color, null, null, "include",
        function () {
            $('#addColorModal').modal('hide')
            fetchColor()
        })
}

function fetchColor() {
    fetch("/getAllColor")
      .then((response) => response.json())
      .then((data) => {
        const dropdown = document.getElementById("customColorDropdown");
        dropdown.innerHTML = "";
  
        data.forEach((item) => {
          const div = document.createElement("div");
          div.className = "dropdown-item d-flex justify-content-between align-items-center";
          div.innerHTML = `
            <span>${item.color}</span>
            <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteColor(${item.color_id})"></i>
          `;
          dropdown.appendChild(div);
        });
  
        const addNew = document.createElement("div");
        addNew.className = "dropdown-item text-primary";
        addNew.setAttribute("data-toggle", "modal");
        addNew.setAttribute("data-target", "#addColorModal");
        addNew.innerHTML = `<i class="fa fa-plus"></i> Thêm Màu mới`;
        dropdown.appendChild(addNew);
      });
  }
  
  function deleteColor(colorId) {
    if (confirm("Bạn có chắc muốn xóa màu này?")) {
      fetch(`/admin/delete_color/${colorId}`, { method: "DELETE" })
        .then((res) => {
          if (res.ok) fetchColor();
          else alert("Xoá thất bại");
        });
    }
  }
  
