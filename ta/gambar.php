<?php

include "koneksi.php";

if(isset($_GET["id_menu"])) {
    $id_menu = $_GET["id_menu"];
    $row = mysqli_fetch_assoc(mysqli_query($koneksi, "SELECT * FROM gambar WHERE id_menu=$id_menu"));
    
    header("Content-Type: image/jpg");
    echo $row["gambar"];
} else {
    echo "<script>alert('id_menu harus disertakan, contoh: ?id_menu=7')</script>";
}