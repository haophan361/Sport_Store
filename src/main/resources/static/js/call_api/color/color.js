
function saveColor() {
  const form = document.getElementById("color-form");
  const formData = new FormData(form);
  const name = document.getElementById("new_color_name").value;
  const images = document.getElementById("color_images").files;

  for (let i = 0; i < images.length; i++) {
      formData.append("images", images[i]);
  }

  let url = "/admin/insert_color";
  let method = "POST";

  if (form.getAttribute("data-mode") === "edit" && selectedColorId) {
      url = `/admin/update_color/${selectedColorId}`;
      method = "PUT";
  }

  fetch(url, {
      method: method,
      body: formData,
      credentials: 'include'
  })
  .then(res => {
      if (res.ok) {
          $('#addColorModal').modal('hide');
          fetchColor();
          form.reset();
          form.removeAttribute("data-mode");
          selectedColorId = null;
      } else {
          alert("Lưu thất bại");
      }
  });
}


let selectedColorId = null; 

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
          <div>
            <i class="fas fa-edit text-info mr-2" style="cursor:pointer;" onclick="editColor(${item.color_id}, '${item.color}')"></i>
            <i class="fas fa-trash-alt text-danger" style="cursor:pointer;" onclick="deleteColor(${item.color_id})"></i>
          </div>
        `;

        div.onclick = function (e) {
          if (e.target.tagName === "I") return;
        
          const btn = div.closest(".dropdown").querySelector(".dropdown-toggle");
          btn.innerText = item.color;
        
          selectedColorId = item.color_id;
        
          const hiddenInput = document.getElementById("colorId");
          if (hiddenInput) hiddenInput.value = item.color_id;
        
          // Load ảnh theo màu vừa chọn
          const imagePreview = document.getElementById("imagePreview");
          imagePreview.innerHTML = "";
          if (item.image_url) {
              const img = document.createElement("img");
              img.src = item.image_url;
              img.classList.add("img-thumbnail", "mr-2");
              img.style.width = "120px";
              img.style.height = "120px";
              img.style.objectFit = "cover";
              imagePreview.appendChild(img);
          } else {
              imagePreview.innerHTML = '<span class="text-muted">Không có ảnh cho màu này.</span>';
          }
        };        

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


function previewImages() {
    const preview = document.getElementById('image-preview');
    preview.innerHTML = '';
    const files = document.getElementById('color_images').files;

    Array.from(files).forEach(file => {
        const reader = new FileReader();
        reader.onload = function (e) {
            const img = document.createElement('img');
            img.src = e.target.result;
            img.classList.add('img-thumbnail');
            img.style.width = '100px';
            img.style.height = '100px';
            img.style.objectFit = 'cover';
            preview.appendChild(img);
        };
        reader.readAsDataURL(file);
    });
}

function editColor(colorId, colorName) {
  selectedColorId = colorId;
  document.getElementById("new_color_name").value = colorName;
  document.getElementById("color-form").setAttribute("data-mode", "edit");

  // Clear preview + ảnh cũ nếu có
  document.getElementById("image-preview").innerHTML = "";
  document.getElementById("color_images").value = "";

  $('#addColorModal').modal('show');
}


function deleteColor(colorId) {
  if (confirm("Bạn có chắc muốn xóa màu này?")) {
      fetch(`/admin/delete_color/${colorId}`, { method: "DELETE" })
          .then(res => {
              if (res.ok) fetchColor();
              else alert("Xoá thất bại");
          });
  }
}

