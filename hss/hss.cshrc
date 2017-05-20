#!/bin/tcsh -f

setenv JAVA_HOME /usr/lib/jvm/java-8-oracle
setenv ANT_HOME /usr/share/ant
setenv MOZILLA_FIVE_HOME /usr/lib/firefox
setenv LD_LIBRARY_PATH ${MOZILLA_FIVE_HOME}:${LD_LIBRARY_PATH} 
setenv PATH {$PATH}:{$ANT_HOME}/bin:/home/jarrett/apache-maven-3.5.0/bin

alias start_hss 'java -cp "/home/jarrett/T7Software/hss/lib/*" app.MainApp'
alias start_client 'java -cp "/home/jarrett/T7Software/hss/lib/*" networking.client.UAVClientService'
alias start_test_cli 'java -cp "/home/jarrett/T7Software/hss/lib/*" networking.server.testing.TestHSSClient'
alias start_test_server 'java -cp "/home/jarrett/T7Software/hss/lib/*" networking.client.testing.TestHSSServer'
alias hss_dir 'cd ~/T7Software/hss'
