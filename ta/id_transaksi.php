<?php

include 'koneksi.php';

$id_transaksi = $_POST['id_transaksi'];

$sql="SELECT id_transaksi FROM transaksi";

echo json_encode($sql);
?>