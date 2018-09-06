cd C:\PhotoBot\
git pull
Start-Process -NoNewWindow mongod --bind_ip_all
java -cp '.;.\Photobot.jar' '-Djava.library.path=C:\PhotoBot\opencv\build\lib\Release' '-Dgate.plugins.home=C:\PhotoBot\GATE\plugins' '-Dgate.site.config=C:\PhotoBot\GATE\gate.xml' -jar Photobot.jar