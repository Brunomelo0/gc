#!/bin/bash
set -e

apt-get update && apt-get install -y docker.io docker-compose git

sudo curl -L "https://github.com/docker/compose/releases/download/1.27.4/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
sudo chmod +x /usr/local/bin/docker-compose
which docker-compose

systemctl start docker

git clone https://github.com/Brunomelo0/gc.git /opt/app
cd /opt/app

cd nu-pix/nu-pix

sudo docker-compose up -d --build
