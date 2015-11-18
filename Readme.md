[![Build Status](https://travis-ci.org/lndobryden/enkive.svg?branch=master)](https://travis-ci.org/lndobryden/enkive)

[![Coverage Status](https://coveralls.io/repos/lndobryden/enkive/badge.svg?branch=master&service=github)](https://coveralls.io/github/lndobryden/enkive?branch=master)

#Spring Tool Suite IDE - Development Envrionment Setup Instructions

1. Download STS	
2. Import - Check out maven project from SCM - git  
   git@github.com:lndobryden/enkive.git
3. Compile Indri and Indri.jar  
   http://wiki.enkive.org/index.php/Installation_Instructions#Indri
4. Set indri.directory at the top of pom.xml to the path used for --prefix when compiling
5. Ensure mongodb is up and running
6. Start Enkive by running com.linuxbox.enkive.MainJettyWebApps  
   It will fail - then in Run Configurations - add environment variable java.library.path to directory specified by indri.library.directory in top of pom


Note: If we could simplify steps 5 and 6 that would be super.