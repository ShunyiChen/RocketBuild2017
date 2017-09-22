1.How to create image file and deploy it to docker container?
mvn package docker:build -DpushImage

2.How to run project image
docker run -p 8761:8761 -t [image file name]

