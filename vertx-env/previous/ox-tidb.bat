set img_name=tidb
set container_name=ox_%img_name%

docker stop %container_name%
docker rm %container_name%
docker rmi %img_name%:latest

docker build -t %img_name%:latest -f ox-tidb .
docker run ^
  -p 4000:4000 ^
  --name %container_name% %img_name%