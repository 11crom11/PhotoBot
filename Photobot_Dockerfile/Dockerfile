FROM microsoft/windowsservercore:ltsc2016

################ DLLs OpenCV #####################
ADD *.dll /Windows/System32/
ADD *.ps1 /
##################################################

################ Chocolatey ######################
ENV chocolateyUseWindowsCompression false
RUN powershell -Command \
    iex ((new-object net.webclient).DownloadString('https://chocolatey.org/install.ps1')); \
    choco feature disable --name showDownloadProgress
RUN choco install -y git
RUN git --version
##################################################

################ Clone PhotoBot Project ##########
RUN git clone https://github.com/11crom11/PhotoBot.git
##################################################

################ Java 8 x64 ######################
ENV JAVA_PKG=server-jre-8u181-windows-x64.tar.gz \
    JAVA_HOME=C:\\jdk1.8.0_181

ADD $JAVA_PKG /
RUN setx /M PATH "%PATH%";"%JAVA_HOME%"\bin
##################################################

################ MongoDB 4.0.1 ######################
SHELL ["powershell", "-Command", "$ErrorActionPreference = 'Stop';"]

ENV MONGO_VERSION 4.0.1
ENV MONGO_DOWNLOAD_URL http://downloads.mongodb.org/win32/mongodb-win32-x86_64-2008plus-ssl-4.0.1-signed.msi
ENV MONGO_DOWNLOAD_SHA256 234bebe451ca4601a8fd5016ede6b3ad424ac059dfcb3d0e76abf934f4143af4

RUN Write-Host ('Downloading {0} ...' -f $env:MONGO_DOWNLOAD_URL); \
	(New-Object System.Net.WebClient).DownloadFile($env:MONGO_DOWNLOAD_URL, 'mongo.msi'); \
	\
	Write-Host ('Verifying sha256 ({0}) ...' -f $env:MONGO_DOWNLOAD_SHA256); \
	if ((Get-FileHash mongo.msi -Algorithm sha256).Hash -ne $env:MONGO_DOWNLOAD_SHA256) { \
		Write-Host 'FAILED!'; \
		exit 1; \
	}; \
	\
	Write-Host 'Installing ...'; \
# https://docs.mongodb.com/manual/tutorial/install-mongodb-on-windows/#install-mongodb-community-edition
	Start-Process msiexec -Wait \
		-ArgumentList @( \
			'/i', \
			'mongo.msi', \
			'/quiet', \
			'/qn', \
			'INSTALLLOCATION=C:\mongodb', \
			'ADDLOCAL=all' \
		); \
	$env:PATH = 'C:\mongodb\bin;' + $env:PATH; \
	[Environment]::SetEnvironmentVariable('PATH', $env:PATH, [EnvironmentVariableTarget]::Machine); \
	\
	Write-Host 'Verifying install ...'; \
	Write-Host '  mongo --version'; mongo --version; \
	Write-Host '  mongod --version'; mongod --version; \
	\
	Write-Host 'Removing ...'; \
	Remove-Item C:\mongodb\bin\*.pdb -Force; \
	Remove-Item C:\windows\installer\*.msi -Force; \
	Remove-Item mongo.msi -Force; \
	\
	Write-Host 'Complete.';

#Descomentar si se desea persistencia en la BBDD
#VOLUME C:\\data\\db C:\\data\\configdb
##################################################

EXPOSE 27017

CMD powershell .\start.ps1