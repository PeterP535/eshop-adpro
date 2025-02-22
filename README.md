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

