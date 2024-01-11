<?php

if($_SERVER['REQUEST_METHOD'] == 'GET') {
include 'koneksi.php';

$a = mysqli_query($koneksi, "SELECT * FROM menu");
    $result = [];
    while($row = mysqli_fetch_array($a)) {
        array_push($result, [
            'id_menu' => $row['id_menu'],
            'nama_menu' => $row['nama_menu'],
            'fee' => $row['harga_menu'],
            'stok' => $row['stok'],
            "link_gambar" => $row["link_gambar"],
            "description" => $row["description"]
        ]);
    }

    echo json_encode($result);
    
    mysqli_close($koneksi);

}

?>