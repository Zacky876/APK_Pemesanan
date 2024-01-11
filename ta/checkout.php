<?php

include "koneksi.php";

$nomor_meja = $_POST["nomor_meja"];
$pajak = $_POST["pajak"];
$total_pembayaran = $_POST["total_pembayaran"];


$total = $total_pembayaran + $pajak;


//tmbah ke tael transaksi
$sql = "INSERT INTO transaksi VALUES (0, '$nomor_meja', CURRENT_TIMESTAMP(), '$total_pembayaran', '$pajak', '$total', 'Belum Dibayar')";

mysqli_query($koneksi, $sql);
echo json_encode(["id_transaksi" => mysqli_insert_id($koneksi)]);