function searchRestaurante(idCategoria){
    var t = document.getElementById("searchType");
    if(idCategoria == null){
        t.value = "TEXTO";
    } else {
        t.value = "CATEGORIA";
        document.getElementById("searchIdCategoria").value = idCategoria;
    }
    document.getElementById("form").submit();
}

function setCmd(cmd){
    document.getElementById("cmd").value = cmd;
    document.getElementById("form").submit();
}

function filterCardapio(categoria) {
    document.getElementById("categoria").value = categoria;
    document.getElementById("filterForm").submit();
}