# Reflection 1

> You already implemented two new features using Spring Boot. Check again your source code and evaluate the coding standards that you have learned in this module. Write clean code principles and secure coding practices that have been applied to your code.  If you find any mistake in your source code, please explain how to improve your code. Please write your reflection inside the repository's README.md file.

Setelah mengimplementasikan fitur edit dan delete di aplikasi Spring Boot ini, saya telah menerapkan beberapa prinsip clean code dan best practices dalam pengembangan perangkat lunak:

Penamaan yang Jelas dan Deskriptif

* Memberikan nama variabel dan fungsi yang mencerminkan tujuan dan kegunaannya
* Menggunakan penamaan yang konsisten di seluruh kode
* Memastikan nama yang dipilih mudah dipahami oleh pengembang lain


Single Responsibility

* Memastikan setiap fungsi memiliki satu tanggung jawab spesifik
* Memisahkan logika bisnis ke dalam layer yang sesuai (Controller, Service, Repository)
* Meningkatkan maintainability dan readability kode


Konsistensi dalam Implementasi

* Mengikuti pola yang sama dalam implementasi fitur edit dan delete seperti yang ada pada fitur create
* Menjaga struktur kode yang seragam untuk memudahkan pemahaman
* Menerapkan standar coding yang konsisten


Prinsip DRY (Don't Repeat Yourself)

* Menghindari duplikasi kode dengan membuat fungsi yang reusable
* Menggunakan abstraksi untuk logika yang sering digunakan
* Memanfaatkan interface dan inheritance secara efektif


Feature Branch Workflow

* Menggunakan branch terpisah untuk pengembangan fitur baru
* Menghindari konflik dengan melakukan isolasi perubahan
* Mempermudah proses review dan testing sebelum merge ke main

Namun, saya menyadari masih ada beberapa aspek yang perlu ditingkatkan, terutama dari sisi keamanan. Saat ini, aplikasi masih rentan terhadap unauthorized access dimana setiap user dapat menghapus produk apapun melalui direct URL access atau brute force. Untuk mengatasi ini, diperlukan implementasi sistem autentikasi dan autorisasi yang dapat:

* Memvalidasi identitas pengguna
* Membatasi akses berdasarkan kepemilikan produk
* Mencegah unauthorized deletion melalui session management


# Reflection 2

> After writing the unit test, how do you feel? How many unit tests should be made in a class? How to make sure that our unit tests are enough to verify our program? It would be good if you learned about code coverage. Code coverage is a metric that can help you understand how much of your source is tested. If you have 100% code coverage, does that mean your code has no bugs or errors? 

Setelah mengimplementasikan unit testing dalam proyek ini, saya mendapatkan beberapa pemahaman penting:

* Manfaat Unit Testing:
    * Meningkatkan kepercayaan terhadap kualitas dan kebenaran kode
    * Sangat penting dalam pengembangan software skala besar (enterprise/startup)
    * Membantu mendeteksi bug dan error lebih awal
    * Menghemat waktu pengembangan dalam jangka panjang

* Jumlah Unit Test yang Diperlukan:

    * Tidak ada aturan baku tentang jumlah unit test yang ideal
    * Kebutuhan unit test bergantung pada kompleksitas program

* Yang terpenting adalah memastikan pengujian mencakup:

    * Fungsi-fungsi utama program
    * Edge cases yang mungkin terjadi
    * Berbagai skenario penggunaan

* Pemahaman tentang Code Coverage:
  * Code coverage 100% tidak menjamin program bebas dari bug
  * Code coverage hanya mengukur seberapa banyak kode yang dieksekusi saat testing
  * Tidak mengukur kebenaran logika atau kualitas pengujian
  * Perlu dikombinasikan dengan pengujian kualitas lainnya

> Suppose that after writing the CreateProductFunctionalTest.java along with the corresponding test case, you were asked to create another functional test suite that verifies the number of items in the product list. You decided to create a new Java class similar to the prior functional test suites with the same setup procedures and instance variables.
What do you think about the cleanliness of the code of the new functional test suite? Will the new code reduce the code quality? Identify the potential clean code issues, explain the reasons, and suggest possible improvements to make the code cleaner! Please write your reflection inside the repository's README.md file.

Dalam konteks pembuatan functional test baru untuk fitur product list, terdapat beberapa pertimbangan clean code yang perlu diperhatikan:

* Identifikasi Masalah:
  * Duplikasi kode setup dan variabel instance antar test class
  * Melanggar prinsip DRY (Don't Repeat Yourself)
  * Menurunkan maintainability kode

* Solusi yang Diusulkan:

  * Membuat base test class yang berisi:

    * Setup procedures umum
    * Variabel instance yang sering digunakan
    * Utility methods yang dapat dipakai ulang

  * Test class spesifik mewarisi dari base class
  * Fokus hanya pada test case yang unik untuk fiturnya

* Keuntungan Pendekatan Ini:

  * Mengurangi duplikasi kode
  * Meningkatkan maintainability
  * Memudahkan penambahan test case baru
  * Menjaga konsistensi antar test suite
  * Memudahkan pembaruan setup procedures