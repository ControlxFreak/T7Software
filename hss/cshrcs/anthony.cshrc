#!/bin/tcsh -f

setenv JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64
setenv ANT_HOME /usr/share/ant
setenv MOZILLA_FIVE_HOME /usr/lib/firefox
#setenv LD_LIBRARY_PATH ${MOZILLA_FIVE_HOME}:${LD_LIBRARY_PATH} 
setenv LD_LIBRARY_PATH ${MOZILLA_FIVE_HOME}
#setenv PATH {$PATH}:{$ANT_HOME}/bin:/home/controlxfreak/apache-maven-3.5.0/bin

alias start_hss 'java -cp "/home/controlxfreak/repos/ELDP/T7Software/hss/lib/*" app.MainApp'
alias start_client 'java -cp "/home/controlxfreak/repos/ELDP/T7Software/hss/lib/*" networking.client.UAVClientService'
alias start_test_cli 'java -cp "/home/controlxfreak/repos/ELDP/T7Software/hss/lib/*" networking.server.testing.TestHSSClient'
alias start_test_server 'java -cp "/home/controlxfreak/repos/ELDP/T7Software/hss/lib/*" networking.client.testing.TestHSSServer'
alias hss_dir 'cd /home/controlxfreak/repos/ELDP/T7Software/hss'
