# Phần mềm Phân tích Hạt

## Giới thiệu
Phần mềm Phân tích Hạt được phát triển để hỗ trợ quá trình đánh giá và lựa chọn giống lúa bằng cách tự động xử lý và phân tích hình ảnh hạt. Công nghệ xử lý ảnh và học máy giúp phần mềm thực hiện phân tích chính xác về kích thước, hình dạng và tỷ lệ chiều dài/rộng của hạt.

Phần mềm thuộc đề tài **CN24.04** của Trường Đại học Công nghệ, Đại học Quốc gia Hà Nội và được phát triển bởi nhóm nghiên cứu Khoa Công nghệ Nông nghiệp.

## Tính năng chính
- **Quản lý dự án**: Tạo, mở và chỉnh sửa thông tin dự án.
- **Chụp mẫu**: Kết nối với máy ảnh và chụp ảnh hạt lúa để phân tích.
- **Phân tích hạt**: Xử lý ảnh để trích xuất các thông tin về kích thước, hình dạng của hạt.
- **Xem kết quả**: Hiển thị ảnh đã phân tích cùng với bảng số liệu.

## Yêu cầu hệ thống
### Phần cứng
- **CPU**: Tối thiểu Intel Core i5 (4 nhân)
- **RAM**: Tối thiểu 8GB
- **GPU** (khuyến nghị): NVIDIA hỗ trợ CUDA (tối thiểu 2GB VRAM)
- **Máy ảnh**: Hỗ trợ nhiều loại, tham khảo danh sách tại [libgphoto2](http://gphoto.org/proj/libgphoto2/support.php)

### Phần mềm
- **Hệ điều hành**: Linux 22.04
- **Java**: Phiên bản 22 trở lên
- **Các thư viện khác**:
    - JavaFX SDK
    - Python (pip, miniconda)

## Hướng dẫn cài đặt
### 1. Cài đặt môi trường Java
1. Tải JDK 22.0.2 từ [Oracle](https://www.oracle.com/java/technologies/javase/jdk22-archive-downloads.html)
2. Cài đặt thông qua **Software Installer**.

### 2. Cài đặt JavaFX SDK
1. Tải từ [Gluon](https://gluonhq.com/products/javafx/)
2. Giải nén và lưu lại đường dẫn thư mục.

### 3. Cài đặt Python và Miniconda
1. Cài **pip**:
   ```sh
   sudo apt install python3-pip
   ```
2. Cài **Miniconda** từ [Anaconda](https://www.anaconda.com/docs/getting-started/miniconda/install#macos-linux-installation).

### 4. Cài đặt phần mềm
1. Tải phần mềm từ GitHub:
   ```sh
   git clone https://github.com/RISLabUET/SeedDesktopLinux2024
   ```
2. Cài đặt môi trường:
   ```sh
   cd SeedDesktopLinux2024
   conda env create -f environment.yml
   conda activate ApplicationLinux
   ```
3. - Tải phần mềm : [ApplicationLinux.jar](https://1drv.ms/u/s!AulvgK0o1PV1uZ1hQD80zt3RlfltqQ?e=EhG6kl) 
   - Xem cách cấu hình thư mục: [HDSD](https://1drv.ms/b/s!AulvgK0o1PV1uYFmWE7MFF14U-Z-Sg?e=c32Ht5)
4. Sửa setting.properties:
- Thay đổi đường dẫn của condaActivatePath. Hiện tại là:
    ```sh
    /home/congnghiep/miniconda3/bin/activate
    ```
  Sang đường dẫn của miniconda trên máy của bạn.
5. Chạy phần mềm:
   ```sh
   java --module-path /path/to/javafx-sdk/lib \
        --add-modules javafx.controls,javafx.fxml \
        -jar /path/to/ApplicationLinux.jar
   ```

## Hướng dẫn sử dụng
### 1. Quản lý dự án
- **Tạo dự án**: Nhập thông tin và nhấn **Tạo dự án**.
- **Mở dự án**: Chọn thư mục dự án và mở tệp `application.properties`.
- **Chỉnh sửa dự án**: Thay đổi thông tin và nhấn **Lưu**.

### 2. Chụp mẫu
- Kết nối máy ảnh và đảm bảo thiết bị được **Unmount**.
- Đặt hạt lúa vào khung hình, nhấn **Chụp ảnh**.
- Xem trước ảnh và chọn **Tiếp tục** hoặc **Chụp lại**.

### 3. Phân tích hạt
- Nhấn **Phân tích**, phần mềm sẽ quét và xử lý toàn bộ hình ảnh trong dự án.

### 4. Xem kết quả
- Hiển thị ảnh phân tích kèm ID và bảng số liệu.
- Có thể xem chi tiết dữ liệu trong thư mục `Result`.

## Đóng góp
Phần mềm là mã nguồn mở, người dùng có thể chỉnh sửa và đóng góp thông qua GitHub. Khuyến nghị sử dụng **IntelliJ Ultimate** và **SceneBuilder** để phát triển.

## Liên hệ
- **Nhóm phát triển**: Khoa Công nghệ Nông nghiệp, Trường Đại học Công nghệ, ĐHQGHN.
- **GitHub**: [RISLabUET/SeedDesktopLinux2024](https://github.com/RISLabUET/SeedDesktopLinux2024)

## Liên hệ
- **Nhóm phát triển**: Khoa Công nghệ Nông nghiệp, Trường Đại học Công nghệ, ĐHQGHN.
- **GitHub**: [RISLabUET/SeedDesktopLinux2024](https://github.com/RISLabUET/SeedDesktopLinux2024)
- **Thành viên nhóm phát triển**:
    - [Phạm Minh Triển](https://github.com/trienpm)
    - [Hà Quang Hưng](https://github.com/hungha1512)
    - [Trần Thanh Hương](https://github.com/imtth79)
    - [Phùng Trường Trinh](https://github.com/PhungTrinhUET)

Chúc bạn sử dụng phần mềm hiệu quả!
