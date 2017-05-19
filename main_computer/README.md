# T7Software
---------------------------------------------------------------------------------------------------------------
Lockheed Martin 
Engineering Leadership Development Program
Team 7
18 May 2017
Anthony Trezza

---------------------------------------------------------------------------------------------------------------

Instalation Guide

1. Clone the repository
$ git clone --recursive https://github.com/ControlxFreak/T7Software.git

** Note: Don't forget the "--recursive".  Without it, you will not receive the necessary protobuf library **

2. Install Protobuff 

$ cd /path/to/T7Software/main_computer/src/OBC/external/protobuf
$ sudo apt-get install autoconf automake libtool curl make g++ unzip
$ ./autogen.sh
$ ./configure
$ make
$ make check
$ sudo make install
$ sudo ldconfig

3. Install Boost Library

$ cd /path/to/T7Software/main_computer/src/OBC/external/boost_1_63_0
$ sudo apt-get install libboost-dev

4. Make
$ cd /path/to/T7Software/main_computer/src/OBC/
$ make

---------------------------------------------------------------------------------------------------------------

Users Guide

1. Build Directives

Numerous preprocessor directives have been built-in for ease of use.  These include:

PRINT_TO_FILE
PRINT_TO_CONSOLE

The names are rather self evident of their funtionality so if you would like the software to do one of these functions, add it to the preprocessor definition list.
Current nomenclature is to add an "N" to the front of it if you do NOT want to use it.  Example: NPRINT_TO_FILE will NOT print to a log file.

2. IP Address / Port number

If for some reason you need to change the ip address or port number, they live as properties of the IOManager class.  Simply edit it and recompile.

Future work includes making that more generic like an input variable into the executive.
