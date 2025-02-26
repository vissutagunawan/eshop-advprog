# Reflection Module 1

## Reflection 1

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


## Reflection 2

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


# Reflection Module 2

> 1. List the code quality issue(s) that you fixed during the exercise and explain your strategy on fixing them.

Dalam proyek ini, saya telah memperbaiki beberapa masalah kualitas kode yang diidentifikasi oleh PMD. Pertama, menghapus modifier public yang tidak perlu pada interface ProductService karena method interface secara default sudah public, yang membuat kode lebih ringkas dan mudah dibaca. Kedua, membersihkan wildcard import yang tidak diperlukan di ProductController dan menggantinya dengan import spesifik, meningkatkan keterbacaan dan maintainability kode. Ketiga, memastikan konsistensi penulisan kode dengan mengikuti Java naming conventions dan struktur kode yang seragam di seluruh project. Semua perbaikan ini dilakukan secara sistematis dengan mengikuti panduan dari PMD dan best practice Java. Implementasi perbaikan ini tidak hanya menyelesaikan masalah kualitas kode yang ada, tetapi juga membuat codebase lebih maintainable untuk pengembangan ke depan.
> 2. Look at your CI/CD workflows (GitHub)/pipelines (GitLab). Do you think the current implementation has met the definition of Continuous Integration and Continuous Deployment? Explain the reasons (minimum 3 sentences)!

Implementasi CI/CD saat ini sudah mencapai definisi Continuous Integration dan Continuous Deployment dengan baik dan komprehensif. Untuk CI, proyek ini memiliki workflow yang mengatur automated testing melalui GitHub Actions, pemeriksaan kualitas kode dengan PMD, dan security scanning dengan Scorecard yang berjalan otomatis setiap ada push atau pull request, memastikan setiap perubahan kode memenuhi standar kualitas yang ditetapkan. Workflow ini juga mencakup pengecekan security vulnerability melalui Scorecard yang berjalan secara terjadwal, memberikan laporan keamanan yang terperinci. Unit testing yang dijalankan secara otomatis memastikan bahwa setiap perubahan kode tidak merusak fungsionalitas yang sudah ada. Untuk CD, proyek ini sudah terintegrasi dengan Koyeb yang secara otomatis melakukan deployment ke production environment setiap kali ada push/merge di branch main, menjamin setiap perubahan yang sudah lolos testing dapat langsung digunakan oleh pengguna. Integrasi dengan Koyeb ini memungkinkan proses deployment yang otomatis dan konsisten, dimana setiap perubahan kode yang lolos CI testing akan langsung di-deploy ke environment production, memenuhi prinsip continuous deployment dan mempercepat proses delivery fitur baru ke pengguna.

# Reflection Module 3

> 1) Explain what principles you apply to your project!

- **S**ingle Responsibility Principle (SRP): Saya menerapkan SRP dengan memisahkan kelas-kelas berdasarkan tanggung jawab tunggal. Contohnya, CarController dan ProductController masing-masing hanya menangani entitas yang sesuai. Controller hanya bertanggung jawab untuk menangani permintaan HTTP, sedangkan logic bisnis berada di service, dan akses data di repository. \
- **O**pen/Closed Principle (OCP): Saya mengimplementasikan OCP dengan membuat interface seperti InterfaceRepository, InterfaceRepositoryRead, dan InterfaceRepositoryWrite. Dengan begitu, implementasi baru dapat ditambahkan tanpa mengubah kode yang sudah ada, seperti CarRepository dan ProductRepository yang menerapkan interface yang sama. \
- **L**iskov Substitution Principle (LSP): LSP diterapkan dengan menghindari inheritance yang tidak tepat. CarController tidak lagi mewarisi dari ProductController, dan repositori menggunakan interface yang sama sehingga mereka dapat saling menggantikan ketika digunakan melalui interface tersebut. \
- **I**nterface Segregation Principle (ISP): ISP diimplementasikan dengan memecah fungsi interface repository menjadi interface-interface yang lebih kecil (InterfaceRepositoryRead dan InterfaceRepositoryWrite), sehingga klien hanya perlu tahu tentang metode yang relevan bagi mereka. \
- **D**ependency Inversion Principle (DIP): DIP diterapkan dengan menginjeksi dependensi melalui abstraksi (interface), bukan implementasi konkrit. Service bergantung pada InterfaceRepository<T> daripada implementasi repository konkrit, dan controller bergantung pada interface service daripada implementasinya.

> 2) Explain the advantages of applying SOLID principles to your project with examples.

- **Peningkatan Modularitas**: Dengan SRP, setiap komponen memiliki tanggung jawab yang jelas. Misalnya, pemisahan CarController dari ProductController memungkinkan pengembangan dan pemeliharaan independen. Jika ada perubahan pada fitur mobil, hanya CarController yang perlu dimodifikasi.
- **Fleksibilitas yang Lebih Baik**: Dengan OCP dan DIP, sistem menjadi lebih fleksibel. Contohnya, kita bisa dengan mudah mengganti implementasi repository (misalnya dari penyimpanan dalam memori ke database) tanpa mengubah kode service, karena service bergantung pada interface repository, bukan implementasi konkritnya.
- **Pengujian yang Lebih Mudah**: Dengan DIP, kita bisa dengan mudah menguji service dengan mocking repository. Misalnya, ketika menguji ProductServiceImpl, kita bisa membuat mock dari InterfaceRepository<Product> daripada menggunakan ProductRepository yang sebenarnya.
- **Pemeliharaan yang Lebih Mudah**: Dengan ISP, perubahan interface menjadi lebih terkendali. Misalnya, jika kita perlu menambahkan fungsionalitas baru ke repository, kita bisa membuat interface baru atau memperluas yang ada tanpa memengaruhi klien yang hanya menggunakan fungsionalitas dasar.
- **Ekstensi yang Lebih Mudah**: Dengan LSP, kita bisa menambahkan implementasi baru dari interface yang ada dengan aman. Misalnya, kita bisa membuat AnotherProductRepository yang mengimplementasikan InterfaceRepository<Product> dan menggunakannya sebagai pengganti ProductRepository tanpa masalah.

> 3) Explain the disadvantages of not applying SOLID principles to your project with examples.

- **Kode yang Sulit Diubah**: Tanpa SRP, perubahan pada satu fitur bisa memengaruhi fitur lain. Misalnya, jika CarController tetap menjadi bagian dari ProductController, perubahan pada logika penanganan produk bisa secara tidak sengaja memengaruhi fungsionalitas mobil.
- **Ketergantungan yang Tinggi**: Tanpa DIP, komponen menjadi sangat terkait. Contohnya, jika ProductServiceImpl langsung bergantung pada ProductRepository alih-alih interface, kita tidak bisa dengan mudah menggantinya dengan implementasi repository lain untuk pengujian atau pengembangan.
- **Sulit untuk Diperluas**: Tanpa OCP, penambahan fitur baru mengharuskan modifikasi kode yang ada. Misalnya, jika kita perlu menambahkan jenis repositori baru, kita harus mengubah kode service yang sudah ada daripada hanya membuat implementasi baru dari interface yang ada.
- **Duplikasi Kode**: Tanpa ISP, interface yang terlalu besar bisa menyebabkan duplikasi kode karena klien harus mengimplementasikan metode yang tidak mereka butuhkan. Misalnya, jika kita hanya memiliki satu interface repository besar, implementasi yang hanya membutuhkan fungsi baca tetap harus mengimplementasikan semua metode tulis.
- **Perilaku yang Tidak Konsisten**: Tanpa LSP, subtipe bisa berperilaku berbeda dari tipe dasarnya. Misalnya, jika CarController mewarisi dari ProductController tetapi mengubah perilaku metode yang diwarisi, ini bisa menyebabkan perilaku yang tidak terduga saat kita mencoba menggunakan CarController sebagai ProductController.
