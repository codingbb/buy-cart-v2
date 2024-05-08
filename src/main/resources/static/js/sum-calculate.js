$(document).ready(function () {
    setTotal();
});

$(".item-checkbox").on("change", function(){
    setTotal();
});

$(".buyQty-change").on("keyup", function(){
    setTotal();
});

/* 총 주문 정보 세팅(총 합계) */
function setTotal(){
    let totalPrice = 0;	// 총 가격

    $("tr.offer-table").each(function(index, element){
        if ($(element).find(".item-checkbox").is(":checked") === true) {
            // 가격에서 콤마를 제거하고 숫자로 변환
            let priceText = $(element).find(".price").text().replace(/,/g, '');
            let price = parseInt(priceText, 10);
            let buyQty = parseInt($(element).find(".buyQty-change").val(), 10);
            // totalPrice += parseInt($(element).find(".price").text());

            //총가격
            totalPrice += price * buyQty;
        }
    });

    // 총 가격
    $(".sum").text(totalPrice.toLocaleString());

}