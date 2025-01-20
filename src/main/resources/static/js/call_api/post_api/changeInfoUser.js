function changeInfoUser()
{
    const form_changeInfoUser=
    {
        name:document.getElementById("name").value,
        date_of_birth:document.getElementById("date_of_birth").value,
        gender: document.querySelector('input[name="gender"]:checked')?.value || null,
        phone:document.getElementById("phone").value,
    }
}
document.addEventListener("DOMContentLoaded", function()
{
    flatpickr("#date_of_birth",
        {
            maxDate:"today",
            enableTime: false,
            dateFormat: "Y-m-d",
        });
});
document.getElementById('addReceiverModal').addEventListener('show.bs.modal', function()
{
    var cities = document.getElementById("cities");
    var districts = document.getElementById("districts");
    var wards = document.getElementById("wards");
    var Parameter =
        {
            url: "/js/call_api/address.json",
            method: "GET",
            responseType: "json",
        };
    var promise = axios(Parameter);
    promise.then(function(result)
    {
        renderCity(result.data);
    });
    function renderCity(data)
    {
        let cityName = document.getElementById("cityName").value;
        let districtName = document.getElementById("districtName").value;
        let wardName = document.getElementById("wardName").value;
        for (const c of data)
        {
            cities.options[cities.options.length] = new Option(c.Name, c.Id);
        }
        let selectedCity = data.find(city => city.Name === cityName);
        if (selectedCity)
        {
            cities.value = selectedCity.Id;
            for (const d of selectedCity.Districts)
            {
                districts.options[districts.options.length] = new Option(d.Name, d.Id);
            }
            let selectedDistrict = selectedCity.Districts.find(district => district.Name === districtName);
            if (selectedDistrict)
            {
                districts.value = selectedDistrict.Id;
                for (const w of selectedDistrict.Wards)
                {
                    wards.options[wards.options.length] = new Option(w.Name, w.Id);
                }
                let selectedWard = selectedDistrict.Wards.find(w => w.Name === wardName);
                if (selectedWard)
                {
                    wards.value = selectedWard.Id;
                }
            }
        }
        cities.onchange = function()
        {
            districts.length = 1;
            wards.length = 1;
            document.getElementById("cityName").value = cities.options[cities.selectedIndex].text;

            if (this.value !== "")
            {
                const selectedCity = data.find(n => n.Id === this.value);
                for (const d of selectedCity.Districts)
                {
                    districts.options[districts.options.length] = new Option(d.Name, d.Id);
                }
            }
        };
        districts.onchange = function()
        {
            wards.length = 1;
            document.getElementById("districtName").value = districts.options[districts.selectedIndex].text;

            const selectedCity = data.find(n => n.Id === cities.value);
            if (this.value !== "")
            {
                const selectedDistrict = selectedCity.Districts.find(n => n.Id === this.value);

                for (const w of selectedDistrict.Wards)
                {
                    wards.options[wards.options.length] = new Option(w.Name, w.Id);
                }
            }
        };
        wards.onchange = function()
        {
            document.getElementById("wardName").value = wards.options[wards.selectedIndex].text;
        };
    }
});
function form_addInfo_Receiver()
{
    document.getElementById("receiver_name").value = "";
    document.getElementById("receiver_phone").value = "";
    document.getElementById("cityName").value="";
    document.getElementById("districtName").value="";
    document.getElementById("wardName").value="";
    document.getElementById("street").value = "";
    document.getElementById("cities").length=1;
    document.getElementById("districts").length=1;
    document.getElementById("wards").length=1;
    document.getElementById("add_button").style.display = "block";
    document.getElementById("update_button").style.display = "none";

    const modal = new bootstrap.Modal(document.getElementById('addReceiverModal'));
    modal.show();
}
function add_infoReceiver()
{
    const form_infoReceiver=
        {
            name:document.getElementById("receiver_name").value,
            phone:document.getElementById("receiver_phone").value,
            city:document.getElementById("cityName").value,
            district:document.getElementById("districtName").value,
            ward:document.getElementById("wardName").value,
            street:document.getElementById("street").value
        }
    fetch("/user/add_infoReceiver",
        {
            method: "POST",
            headers:
                {
                    'Content-type': 'application/json',
                    Authorization: "Bearer " + localStorage.getItem("token")
                },
            body: JSON.stringify(form_infoReceiver),
        })
        .then(response =>
        {
            if (!response.ok)
            {
                return response.text().then(error =>
                {
                    throw new Error(error);
                });
            }
            return response.text();
        })
        .then(data =>
        {
            bootbox.alert({
                title: "Thông báo",
                message: "Thêm thành công",
                callback: function ()
                {
                    location.reload()
                }
            });
        })
        .catch(error =>
        {
            bootbox.alert({
                title: "Cảnh báo lỗi",
                message: "Có lỗi xảy ra: " + error.message
            });
        });
}
function form_updateInfo_Receiver(receiver_id, name, phone, city, district, ward, street)
{
    document.getElementById("receiver_id").value=receiver_id;
    document.getElementById("receiver_name").value = name;
    document.getElementById("receiver_phone").value = phone;
    document.getElementById("cityName").value=city;
    document.getElementById("districtName").value=district;
    document.getElementById("wardName").value=ward;
    document.getElementById("street").value = street;
    document.getElementById("add_button").style.display = "none";
    document.getElementById("update_button").style.display = "block";
    const modal = new bootstrap.Modal(document.getElementById('addReceiverModal'));
    modal.show();
}
function update_infoReceiver()
{
    const form_infoReceiver=
        {
            receiver_id:document.getElementById("receiver_id").value,
            name:document.getElementById("receiver_name").value,
            phone:document.getElementById("receiver_phone").value,
            city:document.getElementById("cityName").value,
            district:document.getElementById("districtName").value,
            ward:document.getElementById("wardName").value,
            street:document.getElementById("street").value
        }
    fetch("/user/update_infoReceiver",
        {
            method: "POST",
            headers:
                {
                    'Content-type': 'application/json',
                    Authorization: "Bearer " + localStorage.getItem("token")
                },
            body: JSON.stringify(form_infoReceiver),
        })
        .then(response =>
        {
            if (!response.ok)
            {
                return response.text().then(error =>
                {
                    throw new Error(error);
                });
            }
            return response.text();
        })
        .then(data =>
        {
            bootbox.alert({
                title: "Thông báo",
                message: "Cập nhật thành công",
                callback: function ()
                {
                    location.reload()
                }
            });
        })
        .catch(error =>
        {
            bootbox.alert({
                title: "Cảnh báo lỗi",
                message: "Có lỗi xảy ra: " + error.message
            });
        });
}
