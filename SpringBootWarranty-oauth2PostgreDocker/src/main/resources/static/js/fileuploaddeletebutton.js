function select(el) {
    img = el;
}
var img;
$(document).ready(function() {

    $("#file").on('change', function() {
        //Get count of selected files
        var countFiles = $(this)[0].files.length;
        var imgPath = $(this)[0].value;
        img = imgPath;
        var extn = imgPath.substring(imgPath.lastIndexOf('.') + 1).toLowerCase();
        var image_holder = $("#midiaDigital");
        image_holder.empty();
        if (extn == "gif" || extn == "png" || extn == "jpg" || extn == "jpeg") {
            if (typeof(FileReader) != "undefined") {
                //loop for each file selected for uploaded.
                for (var i = 0; i < countFiles; i++) {
                    var reader = new FileReader();
                    reader.onload = function(e) {
                        $(image_holder).append('<div class="form-group row">' +
                            '<div>' +
                            '<div class="col-md-6">' +
                            '<img src="' + e.target.result + '" class="thumb-image img-responsive" onclick="select($(this))" style = "width:100px">' +
                            '<input type="hidden" class="form-control input-sm" name="midiaDigitals[' + i + '].legenda" placeholder="Digite a descri??o da m?dia digital"/>' +
                            '<a href="#" class="remove_field1"></a>' + //add input box
                            '</div>' +
                            '</div>' +
                            '</div>');


                    }
                    image_holder.show();
                    reader.readAsDataURL($(this)[0].files[i]);
                }
            } else {
                alert("O browser n?o suporta upload de arquivos.");
            }
        } else {
            alert("Formato de arquivo inv?lido");
        }
    });

    /*$(midiaDigital).on("click", ".remove_field1", function(e) { //user click on remove text
        e.preventDefault();
        $(this).parent('div').remove();
        img.val('');
    })*/

});