# libary-back-end
libary-back-end
Cần cài openssl
Project có sử dụng https, sau khi cài xong https của front-end chạy lệnh: 
openssl pkcs12 -export -in ../library-front-end/localhost.pem -inkey ../library-front-end/localhost-key.pem -name vubaquangkma -out src/main/resources/PKCS12.p12

cài pass là: At130444


trong đó: ../library-front-end/localhost.pem và ../library-front-end/localhost-key.pem là link của 2 file vừa tạo được ở front khi tiến hành config https ở front end 
