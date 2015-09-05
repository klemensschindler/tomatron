#!/bin/bash
mkdir -p ./bin/classes;
cd src;
javac -verbose -d ../bin/classes ./tomatron/model/*.java ./tomatron/controller/*.java  ./tomatron/view/*.java && jar vcmf Manifest.txt ../bin/tomatron -C ../bin/classes .;
cd ..;
cp -f ./resources/icons/48x48/tomatron.png  ~/.local/share/icons/;
chmod +x ./resources/shortcuts/tomatron.desktop;
sudo desktop-file-install --rebuild-mime-info-cache  ./resources/shortcuts/tomatron.desktop
sudo cp -f ./bin/tomatron /usr/local/bin/;
