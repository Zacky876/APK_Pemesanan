<?php
if($_SERVER['REQUEST_METHOD'] == 'GET') {
include 'koneksi.php';

$a = mysqli_query($koneksi, "SELECT * FROM nomor_meja");
    $result = [];
    while($row = mysqli_fetch_array($a)) {
        array_push($result, [
            'id_nomor' => $row['id_nomor'],
            'nomor' => $row['nomor']
        ]);
    }

    echo json_encode($result);
    
    mysqli_close($koneksi);
}
?>