<?php

// Memeriksa apakah data telah dikirimkan dari aplikasi Android
if ($_SERVER["REQUEST_METHOD"] == "GET") {

    include 'koneksi.php';

    $nama_menu = $_GET['nama_menu'];
    $jumlah_menu = $_GET['jumlah_menu'];
    $subtotal = $_GET['subtotal'];
    $nomor_meja = $_GET['nomor_meja'];

    $id_transaksi = mysqli_fetch_assoc(mysqli_query($koneksi, "SELECT id_transaksi FROM transaksi ORDER BY id_transaksi DESC LIMIT 0, 1"))["id_transaksi"];
    
    $id_item = mysqli_fetch_assoc(mysqli_query($koneksi, "SELECT id_menu FROM menu WHERE nama_menu='$nama_menu'"));

    // Menyisipkan data ke tabel detail_transaksi
    $sql = "INSERT INTO detail_pesan VALUES (0, $id_transaksi, '".$id_item['id_menu']."', '$jumlah_menu', '$subtotal', '$nomor_meja')";
    
    // $sql = "UPDATE menu FROM stok - $jumlah_menu WHere id_menu";

    // Perbarui stok di database
    $sql_update_stock = mysqli_query($koneksi, "UPDATE menu SET stok = stok - $jumlah_menu WHERE id_menu = ".$id_item['id_menu']);
    
    if ($koneksi->query($sql) === TRUE) {
        $response = array("status" => "success", "message" => "Checkout berhasil.");
        echo json_encode($response);
    } else {
        $response = array("status" => "error", "message" => "Error: " . $sql . "<br>" . $koneksi->error);
        echo json_encode($response);
    }
    $koneksi->close();
}
?>
