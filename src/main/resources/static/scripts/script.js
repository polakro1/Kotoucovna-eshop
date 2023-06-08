function updateCartItem(cartSessionIndex, quantity) {
    $.ajax({
        type: "POST",
        url: "cart/updateQuantity",
        data: JSON.stringify({cartSessionIndex: cartSessionIndex, quantity: quantity}),
        contentType: "application/json; charset=utf-8",
        dataType: "json",

        success: function (result) {
            $("input[name=quantity" + cartSessionIndex + "]").val(result.quantity);
            $(".sumPrice").html(result.sumPrice + " Kč");
        },
        error: function (jqXHR, textStatus, errorThrown) {
            console.log("Chyba při aktualizaci položky v košíku: " + textStatus + " - " + errorThrown);
        }
    });
}

function sortTable(column) {
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("table");
    switching = true;
    dir = "asc";

    while (switching) {
        switching = false;
        rows = table.getElementsByTagName("tr");

        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;

            x = rows[i].getElementsByTagName("td")[column].innerHTML;
            y = rows[i + 1].getElementsByTagName("td")[column].innerHTML;

            if (dir == "asc") {
                if (table.classList.contains("order-list") && column == 1) {
                    var xDate = moment(x, "DD.MM.YYYY HH:mm").toDate();
                    var yDate = moment(y, "DD.MM.YYYY HH:mm").toDate();
                    if (xDate > yDate) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (x.localeCompare(y, 'cs', {sensitivity: 'base', numeric: 'true'}) > 0) {
                    shouldSwitch = true;
                    break;
                }

            } else if (dir == "desc") {
                if (table.classList.contains("order-list") && column == 1) {
                    var xDate = moment(x, "DD.MM.YYYY HH:mm").toDate();
                    var yDate = moment(y, "DD.MM.YYYY HH:mm").toDate();
                    if (xDate < yDate) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (x.localeCompare(y, 'cs', {sensitivity: 'base', numeric: 'true'}) < 0) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

