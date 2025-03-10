Name    : Peter Putra Lesmana

NPM     : 2306152361

Class    : Adpro A

# Refleksi 1

Merefleksikan perubahan yang telah dilakukan, saya telah memperluas fungsionalitas product management dengan secara cermat mengintegrasikan fitur deletion di samping kemampuan edit yang sudah ada. Ini melibatkan pembaruan repository untuk menyertakan metode yang memungkinkan penghapusan produk berdasarkan ID, memperkuat service layer dengan delete function yang sesuai, serta memodifikasi controller agar dapat menangani deletion melalui endpoint baru. Saya juga memastikan bahwa user interface diperbarui secara intuitif, sehingga setiap product entry kini mencakup tombol "Edit" dan "Delete," lengkap dengan confirmation prompt untuk mencegah accidental deletions.

Proses ini memungkinkan saya untuk memperdalam pemahaman tentang bagaimana business logic layers berinteraksi dengan data persistence dan user interface dalam aplikasi Spring MVC. Saya merasa puas melihat bagaimana perubahan kecil yang dilakukan dengan cermat tidak hanya memperluas fungsionalitas aplikasi, tetapi juga meningkatkan user experience secara keseluruhan. Latihan reflektif ini semakin menegaskan pentingnya incremental development serta pertimbangan yang matang dalam menerapkan fitur yang berdampak langsung pada data integrity dan user interactions.


# Refleksi 2
Setelah menulis unit test, biasanya muncul perasaan lega karena sudah ada mekanisme untuk mendeteksi bug lebih awal, namun tetap ada kekhawatiran apakah setiap scenario, terutama edge cases, telah tercakup dengan baik. Metrik seperti code coverage dapat membantu memantau seberapa banyak baris kode yang diuji, namun 100% code coverage tidak menjamin bahwa kode bebas dari bug karena pengujian tersebut hanya mencakup eksekusi kode, bukan validitas logika atau kasus penggunaan kompleks.

Jika membuat functional test suite baru dengan setup dan instance variables yang sama seperti pada CreateProductFunctionalTest.java, hal ini mengakibatkan code duplication yang melanggar prinsip DRY (Don't Repeat Yourself). Duplikasi tersebut dapat menurunkan maintainability dan meningkatkan risiko kesalahan saat harus melakukan perubahan setup di banyak tempat. Untuk meningkatkan kebersihan kode, sebaiknya ekstrak bagian setup yang umum ke dalam sebuah base class atau helper methods, sehingga setiap test suite hanya fokus pada fungsionalitas spesifik yang diuji, membuat kode lebih modular dan mudah dipelihara.



# Refleksi module 2

Selama latihan, saya memperbaiki beberapa code scanning alerts yang terdeteksi di GitHub, di antaranya:

1. Security Policy dengan menambahkan file SECURITY.md.
2. Branch Protection dengan mengaktifkan branch protection rules agar setiap pull request melalui proses code review.
3. Pinned Dependencies dengan menetapkan versi pasti untuk setiap dependensi guna mencegah build yang tidak konsisten.

Strategi yang saya gunakan adalah memeriksa setiap peringatan secara detail, memahami penyebabnya, lalu menerapkan solusi seperti menambahkan file atau konfigurasi yang sesuai (misalnya, menambahkan SECURITY.md), mengubah pengaturan di GitHub (misalnya, branch protection rules), serta memperbarui Dockerfile atau build configuration lain agar dependensi memiliki versi yang spesifik.
Implementasi GitHub Actions yang ada sudah memenuhi konsep Continuous Integration karena setiap commit atau pull request akan memicu proses build, test, dan analysis secara otomatis. Hal ini memastikan integrasi kode dilakukan secara berkala dan mendeteksi kesalahan lebih cepat. Namun, untuk mencapai Continuous Deployment, diperlukan otomatisasi deployment ke production environment setelah build dan test berhasil, yang mungkin belum sepenuhnya diterapkan saat ini.


# Refleksi module 3

## 1. Prinsip yang Diterapkan dalam Proyek
Proyek ini mengikuti prinsip **SOLID** dalam desain berbasis objek untuk memastikan kode yang mudah dikelola, skalabel, dan memiliki arsitektur yang bersih. Berikut adalah prinsip-prinsip yang diterapkan:

### **1. Single Responsibility Principle (SRP)**
Setiap kelas dalam proyek memiliki satu tanggung jawab utama:
- `Product` → Merepresentasikan entitas produk dengan atributnya.
- `ProductRepository` → Menangani penyimpanan dan pengambilan data.
- `ProductServiceImpl` → Mengelola logika bisnis dan berinteraksi dengan repository.
- `ProductService` & `ReadOnlyProductService` → Mendefinisikan antarmuka service untuk abstraksi yang lebih baik.

### **2. Open/Closed Principle (OCP)**
- Kami memperkenalkan antarmuka `IProductRepository`, memungkinkan berbagai implementasi repository (misalnya, berbasis in-memory atau database) **tanpa mengubah kode yang ada**.
- Contoh: Jika kita beralih dari repository berbasis `ArrayList` ke database, kita cukup membuat kelas baru seperti `DatabaseProductRepository` tanpa perlu mengubah service.

### **3. Liskov Substitution Principle (LSP)**
- Kelas `ProductServiceImpl` mengimplementasikan `ProductService` dengan benar.
- Setiap kelas yang mengimplementasikan `ProductService` dapat digunakan sebagai substitusi tanpa menyebabkan kesalahan.

### **4. Interface Segregation Principle (ISP)**
- Kami membagi antarmuka service menjadi:
    - `ReadOnlyProductService` → Berisi hanya `findAll()` dan `getById()` untuk operasi baca.
    - `ProductService` → Memperluas `ReadOnlyProductService` dan mencakup `create()`, `update()`, dan `delete()`.
- Hal ini memastikan bahwa **klien tidak dipaksa untuk bergantung pada metode yang tidak mereka gunakan**.

### **5. Dependency Inversion Principle (DIP)**
- Alih-alih langsung bergantung pada `ProductRepository`, service sekarang bergantung pada `IProductRepository`.
- Hal ini memungkinkan **dependency injection** yang fleksibel dan meningkatkan arsitektur yang longgar.

## 2. Keuntungan Menerapkan Prinsip SOLID
Menerapkan prinsip SOLID memberikan beberapa keuntungan:

### **1. Maintainability (Kemudahan Perawatan)**
- Contoh: Jika kita perlu menambahkan fitur `discount` ke `Product`, kita hanya perlu memodifikasi kelas `Product` tanpa mempengaruhi `ProductRepository` atau `ProductServiceImpl`.

### **2. Extensibility (Kemudahan Perluasan)**
- Contoh: Untuk menyimpan produk dalam database daripada `ArrayList`, kita cukup membuat `DatabaseProductRepository` yang mengimplementasikan `IProductRepository` **tanpa mengubah service**.

### **3. Scalability (Kemudahan Skalabilitas)**
- Sistem dapat berkembang dengan mudah dengan fitur tambahan, seperti `CategoryService`, tanpa mengganggu fungsionalitas yang ada.

### **4. Testability (Kemudahan Pengujian)**
- Menggunakan antarmuka (`IProductRepository`) memungkinkan **mocking dependencies** dalam unit test, meningkatkan cakupan pengujian dan mengurangi ketergantungan pada implementasi konkret.

### **5. Readability dan Clean Code (Keterbacaan dan Kode yang Bersih)**
- Dengan pemisahan tugas yang jelas (penanganan data, logika bisnis, dan model), proyek tetap **mudah dibaca dan dinavigasi**.

## 3. Kerugian Jika Tidak Menerapkan Prinsip SOLID
Tanpa prinsip SOLID, proyek akan menghadapi beberapa tantangan:

### **1. Tight Coupling (Ketergantungan yang Erat)**
- Tanpa `IProductRepository`, `ProductServiceImpl` akan sangat bergantung pada `ProductRepository`, sehingga sulit untuk mengganti atau memodifikasi mekanisme penyimpanan.

### **2. Code Duplication (Duplikasi Kode)**
- Jika kita tidak memisahkan tanggung jawab dengan benar, banyak kelas mungkin mengimplementasikan logika yang sama (misalnya, filtering produk), menyebabkan **redundansi kode**.

### **3. Rigid and Hard to Change (Sulit Dimodifikasi)**
- Tanpa OCP, mengubah mekanisme penyimpanan repository (dari `List<Product>` ke database) akan membutuhkan perubahan pada `ProductServiceImpl` dan kelas lain yang bergantung padanya.

### **4. Violation of Interface Segregation (Pelanggaran ISP)**
- Jika `ProductService` memiliki satu antarmuka dengan semua metode, klien yang hanya memerlukan `findAll()` akan tetap bergantung pada `create()`, `update()`, dan `delete()`.

### **5. Difficult to Test (Sulit Diuji)**
- Tanpa DIP, unit test untuk `ProductServiceImpl` akan membutuhkan koneksi database nyata daripada menggunakan mock repository, membuat pengujian menjadi **lambat dan kompleks**.

## Kesimpulan
Menerapkan prinsip SOLID memastikan kode yang **bersih, mudah dikelola, dan skalabel**. Dengan mengikuti prinsip-prinsip ini, proyek E-Shop tetap fleksibel untuk pengembangan di masa depan tanpa modifikasi yang tidak perlu pada kode yang sudah ada.
