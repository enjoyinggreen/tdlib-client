#!/bin/sh

#sudo apt-get install -y curl wget git openjdk-21-jdk zlib1g zlib1g-dev \
      #    gcc  make cmake openssl gperf php libssl-dev \
      #    libavformat60 libgl1-mesa-dev libx11-dev pkg-config x11proto-core-dev git libgtk2.0-dev libgtk-3-dev  libxxf86vm-dev \
      #    cmake bison flex gperf ruby ant
sed -i 's+org/drinkless/tdlib+com/rodgers/tdlib+g' td/example/java/CMakeLists.txt
sed -i 's+org.drinkless.tdlib+com.rodgers.tdlib+g' td/example/java/CMakeLists.txt

cd td
rm -rf build
mkdir build
cd build
cmake -DCMAKE_BUILD_TYPE=Debug -DCMAKE_INSTALL_PREFIX:PATH=../example/java/td -DTD_ENABLE_JNI=ON ..
cmake --build . --target install
cd ../example/java
rm -rf build
rm -rf tdlib
mkdir build
mkdir -p com/rodgers/tdlib/example
cp org/drinkless/tdlib/example/Example.java com/rodgers/tdlib/example/
cp org/drinkless/tdlib/Client.java com/rodgers/tdlib/
sed -i 's+org.drinkless.tdlib+com.rodgers.tdlib+g' com/rodgers/tdlib/example/Example.java
sed -i 's+org.drinkless.tdlib+com.rodgers.tdlib+g' com/rodgers/tdlib/Client.java
cd build
cmake -DCMAKE_BUILD_TYPE=Release -DCMAKE_INSTALL_PREFIX:PATH=../tdlib -DTd_DIR:PATH=$(readlink -e ../td/lib/cmake/Td) ..
cmake --build . --target install

# sudo mkdir -p /usr/java/packages/lib
# sudo cp td/example/java/tdlib/bin/libtdjni.so /usr/java/packages/lib/
# cp -r td/example/java/com app/src/main/java/
