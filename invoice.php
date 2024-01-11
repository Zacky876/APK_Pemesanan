<?php

include "koneksi.php";

$id_transaksi = $_GET['id_transaksi'];

// $id_item = mysqli_fetch_assoc(mysqli_query($koneksi, "SELECT id_transaksi FROM detail_pesan WHERE id_transaksi='$id_transaksi'"));

// $sql = mysqli_fetch_assoc(mysqli_query($koneksi, "SELECT * FROM detail_pesan WHERE id_transaksi = $id_transaksi AND tgl = '$tgl' AND id_menu = '".$id_item['id_menu']."' AND harga_menu = $harga_menu AND jumlah_menu = '$jumlah_menu' AND subtotal = '$subtotal'"));
$sql = mysqli_query( $koneksi, "SELECT transaksi.item_total,transaksi.pajak,transaksi.total_pembayaran,
        detail_pesan.id_transaksi,detail_pesan.id_menu,detail_pesan.jumlah_menu,detail_pesan.subTotal,
        menu.nama_menu,menu.harga_menu,transaksi.tanggal_pembayaran
        FROM transaksi
        INNER JOIN detail_pesan ON transaksi.id_transaksi = detail_pesan.id_transaksi 
        INNER JOIN menu ON detail_pesan.id_menu = menu.id_menu
        WHERE transaksi.id_transaksi = '$id_transaksi'");

$data = [];

for($i=0; ($data[$i] = mysqli_fetch_assoc($sql)) != null; $i++);
array_pop($data);

echo json_encode($data);

// mysqli_query($koneksi, $sql);
