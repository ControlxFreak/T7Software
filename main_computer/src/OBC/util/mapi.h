/*
*	API for wireless cards under Linux  Version 1.0 - Nov, 2001
*
*		Moustafa A. Youssef - MIND Lab '01
*
* This API works for any driver that supports wireless extensions (provided that the driver works correctly)
* For the signal strength measurements from all the access points, only the mwavelan_cs driver works (as far as I know)
*
* Read the file: MAPI_README.txt
* 
* Code based on the Wireless Tools By
* 	Jean II - HPLB '99
*/ 
	

#include "iwcommon.h"		/* Header */
/*
* Get the latest statistics for the current access point
*/
int GetCurrentAPStats(char* InterfaceName, iwstats*	Stats);

/*
* Get wireless informations & config from the device driver
*/
int GetInterfaceInfo(char* InterfaceName, struct wireless_info*	Info);

/*
* Get Wireless Name
*/
int GetWirelessName(char* InterfaceName, char * Name);

/*
* Get Frequency
*/
int GetFrequency(char* InterfaceName, float* Freq);

/*
* Get Access Point Address
*/
int GetAPAddress(char* InterfaceName, char* APAddress);

/*
* Get Operation Mode
*/
int GetOperationMode(char* InterfaceName, int* OperationMode);

/*
* Get All network interfaces names
*/
void GetNetworkInterfacesNames(char* Names, int* Number);

/*
* Get Wireless network interfaces names
*/
void GetWirelessInterfacesNames(char* Names, int* Number);

/*
* Print on the screen in a neat fashion all the statistics we have collected
* on an interface.
*/
void DisplayStat(char*	InterfaceName, iwstats*	Stats);

/*
* Print to a file in a neat fashion all the statistics we have collected
* on an interface.
*/
void DisplayStatFile(FILE*, char*	InterfaceName, iwstats*	Stats);

/*
* Print on the screen in a neat fashion all the Info we have collected
* on a device.
*/
void DisplayInfo(char* InterfaceName, struct wireless_info*	Info);

/*
* Print on the screen in a neat fashion all the Info we have collected
* on a device.
*/
void PrintInfo(char* InterfaceName);

/*
* Get Info on all devices and print it on the screen
*/
void PrintDevices();

int SetInfo(char *		InterfaceName,	/* Dev name */ 
	char *		Args[],	        /* Command line Args */
	int		Count);		/* Args Count */

/*
* Opens a socket for talking with the networking kernel
*/
int OpenSocket();

void CloseSocket();
void Initialize();

/*
	Add the given address to the spy list
*/
int RegisterAddress(char * Address, char * InterfaceName);
/*------------------------------------------------------------------
 * Display the spy list of addresses and the associated stats
 */
 int GetAddressStats(char *  InterfaceName, char* Address, struct iw_quality* Stats);


/*
 *  Get all the access points information
 *  Returns number of AP found, -1 if error
 */
int GetAllAPInformation(char* InterfaceName,  struct sockaddr * Address, struct iw_quality* Qual, int* HasQual);
/* 
 * Display to the screen all AP information that we have collected
 */
void DisplayAllAPInfo(char * InterfaceName,  int n, struct sockaddr * Address, struct iw_quality* Qual, int HasQual);

/*
 *  Issues a private command to the driver
 */
int PrivateCommand(char * InterfaceName,        /* Dev name */
                   char * Args[],		/* Command line args */
		   int	  Count);		/* Args count */















