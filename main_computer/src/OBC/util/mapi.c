/*
*	API for wireless cards under Linux Version 1.0 - Nov, 2001
*
*		Moustafa A. Youssef - MIND Lab '01
*
* This API works for any driver that supports wireless extensions (provided that the driver works correctly)
* For the signal strength measurements from all the access points, only the mwavelan_cs driver works (as far as I know)
* Read the file: MAPI_README.txt
* 
* Code based on the Wireless Tools By
* 	Jean II - HPLB '99
*/ 
	
#include "mapi.h"
#include <string.h>

/**************************** VARIABLES ****************************/
char *	OperationMode[] = { "Auto",
"Ad-Hoc",
"Managed",
"Master",
"Repeater",
"Secondary" };

int SocketFileDescriptor = -1;		/* generic raw socket desc.	*/

/************************* Information Routines **************************/

/*------------------------------------------------------------------*/
/*
*   Read /proc/net/wireless to get the latest statistics for the current access point
*	MOS
*   Input:
		- InterfaceName: Interface name (e.g. eth0)
	Outpu:Stats
		- Stats:	Collected Statistics
		- returns: 0 if successful, -1 otherwise 
		
*/

int GetCurrentAPStats(char * InterfaceName, iwstats * Stats)
{
	FILE *	f=fopen("/proc/net/wireless","r");
	char		buf[256];
	char *	bp;
	int		t;
	if(f==NULL)
		return -1;
	/* Loop on all devices */
	while(fgets(buf,255,f)){
		bp=buf;
		while(*bp&&isspace(*bp))					/* MOS: skip blanks	*/
			bp++;
		/* Is it the good device ? */
		if(strncmp(bp, InterfaceName, strlen(InterfaceName))==0 && bp[strlen(InterfaceName)]==':')
		{
			/* Skip ethX: */
			bp=strchr(bp,':');
			bp++;
			/* -- status -- */
			bp = strtok(bp, " ");					/* MOS: tokenize by space */
			sscanf(bp, "%X", &t);
			Stats->status = (unsigned short) t;
			/* -- link quality -- */
			bp = strtok(NULL, " ");
			if(strchr(bp,'.') != NULL)				/* MOS: '.' means updated */
				Stats->qual.updated |= 1;
			sscanf(bp, "%d", &t);
			Stats->qual.qual = (unsigned char) t;
			/* -- signal level -- */
			bp = strtok(NULL, " ");
			if(strchr(bp,'.') != NULL)
				Stats->qual.updated |= 2;
			sscanf(bp, "%d", &t);
			Stats->qual.level = (unsigned char) t;
			/* -- noise level -- */
			bp = strtok(NULL, " ");
			if(strchr(bp,'.') != NULL)
				Stats->qual.updated += 4;
			sscanf(bp, "%d", &t);
			Stats->qual.noise = (unsigned char) t;
			/* -- discarded packets -- */
			bp = strtok(NULL, " ");
			sscanf(bp, "%d", &Stats->discard.nwid);
			bp = strtok(NULL, " ");
			sscanf(bp, "%d", &Stats->discard.code);
			bp = strtok(NULL, " ");
			sscanf(bp, "%d", &Stats->discard.misc);
			fclose(f);
			return 0;
		}
    }
	fclose(f);
	return -1;
}

/*------------------------------------------------------------------*/
/*
* Get wireless informations & config from the device driver
* We will call all the classical wireless ioctl on the driver through
* the socket to know what is supported and to get the settings... 
*  MOS 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- Info: Collected information (including flags to indicate if the property is supported or not) 
		- returns: -ve if unsuccessful
*/
 
int GetInterfaceInfo(char* InterfaceName, struct wireless_info*	Info)
{
	struct iwreq wrq;
	
	memset((char *) Info, 0, sizeof(struct wireless_info));
	
	/* Get wireless name */
	strcpy(wrq.ifr_name, InterfaceName);
 
	if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0)
		/* If no wireless name : no wireless extensions */
		return(-1);
	else
		strcpy(Info->name, wrq.u.name);
	
	/* Get network ID */
	strcpy(wrq.ifr_name, InterfaceName);
	if(ioctl(SocketFileDescriptor, SIOCGIWNWID, &wrq) >= 0)
    {
		Info->has_nwid = 1;
		memcpy(&(Info->nwid), &(wrq.u.nwid), sizeof(iwparam));
    }
	
	/* Get frequency / channel */
	strcpy(wrq.ifr_name, InterfaceName);
	if(ioctl(SocketFileDescriptor, SIOCGIWFREQ, &wrq) >= 0)
    {
		Info->has_freq = 1;
		Info->freq = freq2float(&(wrq.u.freq));
    }
	
	/* Get sensitivity */
	strcpy(wrq.ifr_name, InterfaceName);
	if(ioctl(SocketFileDescriptor, SIOCGIWSENS, &wrq) >= 0)
    {
		Info->has_sens = 1;
		memcpy(&(Info->sens), &(wrq.u.sens), sizeof(iwparam));
    }
	
	/* Get encryption information */
	strcpy(wrq.ifr_name, InterfaceName);
	wrq.u.data.pointer = (caddr_t) Info->key;
	wrq.u.data.length = 0;
	wrq.u.data.flags = 0;
	if(ioctl(SocketFileDescriptor, SIOCGIWENCODE, &wrq) >= 0)
    {
		Info->has_key = 1;
		Info->key_size = wrq.u.data.length;
		Info->key_flags = wrq.u.data.flags;
    }
	
	/* Get ESSID */
	strcpy(wrq.ifr_name, InterfaceName);
	wrq.u.essid.pointer = (caddr_t) Info->essid;
	wrq.u.essid.length = 0;
	wrq.u.essid.flags = 0;
	if(ioctl(SocketFileDescriptor, SIOCGIWESSID, &wrq) >= 0)
    {
		Info->has_essid = 1;
		Info->essid_on = wrq.u.data.flags;
    }
	
	/* Get AP address */
	strcpy(wrq.ifr_name, InterfaceName);
	if(ioctl(SocketFileDescriptor, SIOCGIWAP, &wrq) >= 0)
    {
		Info->has_ap_addr = 1;
		memcpy(&(Info->ap_addr), &(wrq.u.ap_addr), sizeof (sockaddr));
    }
	
	/* Get NickName */
	strcpy(wrq.ifr_name, InterfaceName);
	wrq.u.essid.pointer = (caddr_t) Info->nickname;
	wrq.u.essid.length = 0;
	wrq.u.essid.flags = 0;
	if(ioctl(SocketFileDescriptor, SIOCGIWNICKN, &wrq) >= 0)
		if(wrq.u.data.length > 1)
			Info->has_nickname = 1;
		
		/* Get bit rate */
		strcpy(wrq.ifr_name, InterfaceName);
		if(ioctl(SocketFileDescriptor, SIOCGIWRATE, &wrq) >= 0)
		{
			Info->has_bitrate = 1;
			memcpy(&(Info->bitrate), &(wrq.u.bitrate), sizeof(iwparam));
		}
		
		/* Get RTS threshold */
		strcpy(wrq.ifr_name, InterfaceName);
		if(ioctl(SocketFileDescriptor, SIOCGIWRTS, &wrq) >= 0)
		{
			Info->has_rts = 1;
			memcpy(&(Info->rts), &(wrq.u.rts), sizeof(iwparam));
		}
		
		/* Get fragmentation threshold */
		strcpy(wrq.ifr_name, InterfaceName);
		if(ioctl(SocketFileDescriptor, SIOCGIWFRAG, &wrq) >= 0)
		{
			Info->has_frag = 1;
			memcpy(&(Info->frag), &(wrq.u.frag), sizeof(iwparam));
		}
		
		/* Get operation mode */
		strcpy(wrq.ifr_name, InterfaceName);
		if(ioctl(SocketFileDescriptor, SIOCGIWMODE, &wrq) >= 0)
		{
			if((wrq.u.mode < 6) && (wrq.u.mode >= 0))
				Info->has_mode = 1;
			Info->mode = wrq.u.mode;
		}
		
		/* Get Power Management settings */
		strcpy(wrq.ifr_name, InterfaceName);
		if(ioctl(SocketFileDescriptor, SIOCGIWPOWER, &wrq) >= 0)
		{
			Info->has_power = 1;
			memcpy(&(Info->power), &(wrq.u.power), sizeof(iwparam));
		}
		
		/* Get Stats */
		if(GetCurrentAPStats(InterfaceName, &(Info->stats)) >= 0)
		{
			Info->has_stats = 1;
		}
		
		/* Get ranges */
		if(get_range_info(SocketFileDescriptor, InterfaceName, &(Info->range)) >= 0)
			Info->has_range = 1;
		
		return(0);
}
 
/*------------------------------------------------------------------*/ 
/* 
*  MOS 
* Get Wireless Name 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- Wireless Name 
		- returns: -ve if unsuccessful 
*/ 
 
int GetWirelessName(char* InterfaceName, char * Name) 
{ 
	struct iwreq wrq; 
	 
	/* Get wireless name */ 
	strcpy(wrq.ifr_name, InterfaceName); 
 
	if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0) 
		/* If no wireless name : no wireless extensions */ 
		return(-1); 
	else 
		strcpy(Name, wrq.u.name); 
	return(0); 
} 
/* 
	Rest of Subfunctions 
*/ 
 
/*------------------------------------------------------------------*/ 
/* 
* MOS 
* Get Frequency 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- Freq: Frequency in HErtz 
		- returns: -ve if unsuccessful 
*/ 
 
int GetFrequency(char* InterfaceName, float* Freq) 
{ 
	struct iwreq wrq; 
	 
	/* Get wireless name */ 
	strcpy(wrq.ifr_name, InterfaceName); 
 
	if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0) 
		/* If no wireless name : no wireless extensions */ 
		return(-1); 
	/* Get frequency / channel */ 
	strcpy(wrq.ifr_name, InterfaceName); 
	if(ioctl(SocketFileDescriptor, SIOCGIWFREQ, &wrq) >= 0) 
    { 
		*Freq = freq2float(&(wrq.u.freq)); 
		return 0; 
    } 
	return (-1); 
} 
 
/*------------------------------------------------------------------*/ 
/* 
* MOS 
* Get Access Point Address 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- Freq: Access point MAC address 
		- returns: -ve if unsuccessful 
*/ 
 
int GetAPAddress(char* InterfaceName, char* APAddress) 
{ 
	struct iwreq wrq; 
	 
	//	memset((char *) Info, 0, sizeof(struct wireless_info)); 
	 
	/* Get wireless name */ 
	strcpy(wrq.ifr_name, InterfaceName); 
 
	if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0) 
		/* If no wireless name : no wireless extensions */ 
		return(-1); 
	strcpy(wrq.ifr_name, InterfaceName); 
	/* Get AP address */ 
	strcpy(wrq.ifr_name, InterfaceName); 
	if(ioctl(SocketFileDescriptor, SIOCGIWAP, &wrq) >= 0){
		sockaddr Addr;
		memcpy(&Addr, &wrq.u.ap_addr, sizeof(sockaddr));  
		strcpy(APAddress,pr_ether((unsigned char*)&Addr.sa_data)); 
		return 0; 
    } 
	return (-1); 
} 
 
/*------------------------------------------------------------------*/ 
/* 
* MOS 
* Get Operation Mode 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- Operation Mode: One of the 6 operation modes defined at the begining of this file 
		- returns: -ve if unsuccessful 
*/ 
 
int GetOperationMode(char* InterfaceName, int* OperationMode) 
{ 
	struct iwreq wrq; 
	 
	//	memset((char *) Info, 0, sizeof(struct wireless_info)); 
	 
	/* Get wireless name */ 
	strcpy(wrq.ifr_name, InterfaceName); 
 
	if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0) 
		/* If no wireless name : no wireless extensions */ 
		return(-1); 
	/* Get operation mode */ 
	strcpy(wrq.ifr_name, InterfaceName); 
	if(ioctl(SocketFileDescriptor, SIOCGIWMODE, &wrq) >= 0){ 
		if((wrq.u.mode < 6) && (wrq.u.mode >= 0)) 
		*OperationMode= wrq.u.mode; 
		return 0; 
	} 
	return (-1); 
} 
 
/*------------------------------------------------------------------*/
/*
* Print on the screen in a neat fashion all the Info we have collected
* on a device.
*  MOS  
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
		- Info: Information about the interface.
	Output: 
		- None
	     
*/

void DisplayInfo(char* InterfaceName, struct wireless_info*	Info)
{
	/* Display device name and wireless name (name of the protocol used) */
	printf("%-8.8s  %s  ", InterfaceName, Info->name);
	
	/* Display ESSID (extended network), if any */
	if(Info->has_essid)
    {
		if(Info->essid_on)
			printf("ESSID:\"%s\"  ", Info->essid);
		else
			printf("ESSID:off  ");
    }
	
	/* Display NickName (station name), if any */
	if(Info->has_nickname)
		printf("Nickname:\"%s\"", Info->nickname);
	
	/* Formatting */
	if(Info->has_essid || Info->has_nickname)
		printf("\n          ");
	
	/* Display Network ID */
	if(Info->has_nwid)
    {
	/* Note : should display right number of digit according to Info
		* in range structure */
		if(Info->nwid.disabled)
			printf("NWID:off/any  ");
		else
			printf("NWID:%X  ", Info->nwid.value);
    }
	
	/* Display frequency / channel */
	if(Info->has_freq)
    {
		if(Info->freq < KILO)
			printf("Channel:%g  ", Info->freq);
		else
		{
			if(Info->freq >= GIGA)
				printf("Frequency:%gGHz  ", Info->freq / GIGA);
			else
			{
				if(Info->freq >= MEGA)
					printf("Frequency:%gMHz  ", Info->freq / MEGA);
				else
					printf("Frequency:%gkHz  ", Info->freq / KILO);
			}
		}
    }
	
	/* Display sensitivity */
	if(Info->has_sens)
    {
		/* Fixed ? */
		if(Info->sens.fixed)
			printf("Sensitivity=");
		else
			printf("Sensitivity:");
		
		if(Info->has_range)
			/* Display in dBm ? */
			if(Info->sens.value < 0)
				printf("%d dBm  ", Info->sens.value);
			else
				printf("%d/%d  ", Info->sens.value, Info->range.sensitivity);
			else
				printf("%d  ", Info->sens.value);
    }
	
	/* Display the current mode of operation */
	if(Info->has_mode)
    {
		/* A bit of clever formatting */
		if((Info->has_nwid + 2*Info->has_freq + 2*Info->has_sens
			+ !Info->has_essid) > 4)
			printf("\n          ");
		
		printf("Mode:%s  ", OperationMode[Info->mode]);
    }
	
	/* Display the address of the current Access Point */
	if(Info->has_ap_addr)
    {
		/* A bit of clever formatting */
		if((Info->has_nwid + 2*Info->has_freq + 2*Info->has_sens
			+ Info->has_mode + !Info->has_essid) > 3)
			printf("\n          ");
		
		printf("Access Point: %s", pr_ether(Info->ap_addr.sa_data));
    }
	
	printf("\n          ");
	
	/* Display the currently used/set bit-rate */
	if(Info->has_bitrate)
    {
		/* Fixed ? */
		if(Info->bitrate.fixed)
			printf("Bit Rate=");
		else
			printf("Bit Rate:");
		
		if(Info->bitrate.value >= GIGA)
			printf("%gGb/s", Info->bitrate.value / GIGA);
		else
			if(Info->bitrate.value >= MEGA)
				printf("%gMb/s", Info->bitrate.value / MEGA);
			else
				printf("%gkb/s", Info->bitrate.value / KILO);
			printf("   ");
    }
	
	/* Display the RTS threshold */
	if(Info->has_rts)
    {
		/* Disabled ? */
		if(Info->rts.disabled)
			printf("RTS thr:off   ");
		else
		{
			/* Fixed ? */
			if(Info->rts.fixed)
				printf("RTS thr=");
			else
				printf("RTS thr:");
			
			printf("%d B   ", Info->rts.value);
		}
    }
	
	/* Display the fragmentation threshold */
	if(Info->has_frag)
    {
		/* Disabled ? */
		if(Info->frag.disabled)
			printf("Fragment thr:off   ");
		else
		{
			/* Fixed ? */
			if(Info->frag.fixed)
				printf("Fragment thr=");
			else
				printf("Fragment thr:");
			
			printf("%d B   ", Info->frag.value);
		}
    }
	
	/* Formating */
	if((Info->has_bitrate) || (Info->has_rts) || (Info->has_bitrate))
		printf("\n          ");
	
	/* Display encryption information */
	/* Note : we display only the "current" key, use iwspy to list all keys */
	if(Info->has_key)
    {
		printf("Encryption key:");
		if((Info->key_flags & IW_ENCODE_DISABLED) || (Info->key_size == 0))
			printf("off\n          ");
		else
		{
			int	i;
			
			printf("%.2X", Info->key[0]);
			for(i = 1; i < Info->key_size; i++)
			{
				if((i & 0x1) == 0)
					printf("-");
				printf("%.2X", Info->key[i]);
			}
			
			/* Other Info... */
			if((Info->key_flags & IW_ENCODE_INDEX) > 1)
				printf(" [%d]", Info->key_flags & IW_ENCODE_INDEX);
			if(Info->key_flags & IW_ENCODE_RESTRICTED)
				printf("   Encryption mode:restricted");
			if(Info->key_flags & IW_ENCODE_OPEN)
				printf("   Encryption mode:open");
			printf("\n          ");
		}
    }
	
	/* Display Power Management information */
	/* Note : we display only one parameter, period or timeout. If a device
	* (such as HiperLan) has both, we would need to be a bit more clever... */
	if(Info->has_power)	/* I hope the device has power ;-) */
    { 
		printf("Power Management");
		/* Disabled ? */
		if(Info->power.disabled)
			printf(":off\n          ");
		else
		{
			/* Let's check the value and its type */
			if(Info->power.flags & IW_POWER_TYPE)
			{
				/* Type */
				if(Info->power.flags & IW_POWER_TIMEOUT)
					printf(" timeout:");
				else
					printf(" period:");
				
				/* Display value with units */
				if(Info->power.value >= (int) MEGA)
					printf("%gs  ", ((double) Info->power.value) / MEGA);
				else
					if(Info->power.value  >= (int) KILO)
						printf("%gms  ", ((double) Info->power.value) / KILO);
					else
						printf("%dus  ", Info->power.value);
			}
			
			/* Let's check the mode */
			switch(Info->power.flags & IW_POWER_MODE)
			{
			case IW_POWER_UNICAST_R:
				printf(" mode:Unicast received");
				break;
			case IW_POWER_MULTICAST_R:
				printf(" mode:Multicast received");
				break;
			case IW_POWER_ALL_R:
				printf(" mode:All packets received");
				break;
			case IW_POWER_FORCE_S:
				printf(" mode:Force sending");
				break;
			case IW_POWER_REPEATER:
				printf(" mode:Repeat multicasts");
				break;
			default:
			}
			
			/* Let's check if nothing (simply on) */
			if(Info->power.flags == IW_POWER_ON)
				printf(":on");
			printf("\n          ");
		}
    }
	
	if(Info->has_stats)
    {
		if(Info->has_range && (Info->stats.qual.level != 0))
			/* If the statistics are in dBm */
			if(Info->stats.qual.level > Info->range.max_qual.level)
				printf("Link quality:%d/%d  Signal level:%d dBm  Noise level:%d dBm\n",
				Info->stats.qual.qual, Info->range.max_qual.qual,
				Info->stats.qual.level - 0x100,
				Info->stats.qual.noise - 0x100);
			else
				/* Statistics are relative values (0 -> max) */
				printf("Link quality:%d/%d  Signal level:%d/%d  Noise level:%d/%d\n",
				Info->stats.qual.qual, Info->range.max_qual.qual,
				Info->stats.qual.level, Info->range.max_qual.level,
				Info->stats.qual.noise, Info->range.max_qual.noise);
			else
				/* We can't read the range, so we don't know... */
				printf("Link quality:%d  Signal level:%d  Noise level:%d\n",
				Info->stats.qual.qual,
				Info->stats.qual.level,
				Info->stats.qual.noise);
			
			printf("          Rx invalid nwid:%d  invalid crypt:%d  invalid misc:%d\n",
				Info->stats.discard.nwid,
				Info->stats.discard.code,
				Info->stats.discard.misc);
    }
	
	printf("\n");
}

/*------------------------------------------------------------------*/
/*
* Print on the screen in a neat fashion all the Info we have collected
* on a device.
 MOS 
* Get Frequency 
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
	Output: 
		- None

*/
void PrintInfo(char* InterfaceName)
{
	struct wireless_info	Info;
	
	if(GetInterfaceInfo(InterfaceName, &Info) < 0)
    {
		fprintf(stderr, "%-8.8s  no wireless extensions.\n\n",
			InterfaceName);
		return;
    }
	
	/* Display it ! */
	DisplayInfo(InterfaceName, &Info);
}
/*------------------------------------------------------------------*/ 
/* 
* Get Info on all devices and print it on the screen 
*/ 
void PrintDevices() 
{ 
	char buff[1024]; 
	struct ifconf ifc; 
	struct ifreq *ifr; 
	int i; 
	 
	/* Get list of active devices */ 
	ifc.ifc_len = sizeof(buff); 
	ifc.ifc_buf = buff; 
	if(ioctl(SocketFileDescriptor, SIOCGIFCONF, &ifc) < 0) 
    { 
		fprintf(stderr, "SIOCGIFCONF: %s\n", strerror(errno)); 
		return; 
    } 
	ifr = ifc.ifc_req; 
	 
	/* Print them */ 
	for(i = ifc.ifc_len / sizeof(struct ifreq); --i >= 0; ifr++) 
		PrintInfo(ifr->ifr_name); 
} 
 
/*------------------------------------------------------------------*/ 
/* 
* MOS 
* Get All network interfaces names 
* Input: 
*	- None 
* Output: 
*	- Names: A list of interface names separated by the new line character 
*	- Number: Number of Interfaces found 
*/ 
void GetNetworkInterfacesNames(char* Names, int* Number) 
{ 
	char buff[1024]; 
	struct ifconf ifc; 
	struct ifreq *ifr; 
	int i; 
	 
	/* Get list of active devices */ 
	ifc.ifc_len = sizeof(buff); 
	ifc.ifc_buf = buff; 
	if(ioctl(SocketFileDescriptor, SIOCGIFCONF, &ifc) < 0) 
    { 
		fprintf(stderr, "SIOCGIFCONF: %s\n", strerror(errno)); 
		return; 
    } 
	ifr = ifc.ifc_req; 
	 
	/* Return them */ 
	*Number= 0; 
	Names[0]= '\0'; 
 
	for(i = ifc.ifc_len / sizeof(struct ifreq); --i >= 0; ifr++){ 
		strcat(Names, ifr->ifr_name); 
		strcat(Names, "\n"); 
		(*Number)++; 
	} 
} 
 
/*------------------------------------------------------------------*/ 
/* 
* MOS 
* Get Wireless network interfaces names 
* Input: 
*	- None 
* Output: 
*	- Names: A list of wireless interface names separated by the new line character 
*	- Number: Number of wireless interfaces found 
*/ 
void GetWirelessInterfacesNames(char* Names, int* Number) 
{ 
	char buff[1024]; 
	struct ifconf ifc; 
	struct ifreq *ifr; 
	int i; 
	 
	/* Get list of active devices */ 
	ifc.ifc_len = sizeof(buff); 
	ifc.ifc_buf = buff; 
	if(ioctl(SocketFileDescriptor, SIOCGIFCONF, &ifc) < 0) 
    { 
		fprintf(stderr, "SIOCGIFCONF: %s\n", strerror(errno)); 
		return; 
    } 
	ifr = ifc.ifc_req; 
	 
	/* Return them */ 
	*Number= 0; 
	Names[0]= '\0'; 
 
	for(i = ifc.ifc_len / sizeof(struct ifreq); --i >= 0; ifr++){ 
		struct iwreq wrq; 
		/* Get wireless name */ 
		strcpy(wrq.ifr_name, ifr->ifr_name); 
 
		if(ioctl(SocketFileDescriptor, SIOCGIWNAME, &wrq) < 0) 
			/* If no wireless name : no wireless extensions */ 
			continue; 
		strcat(Names, ifr->ifr_name); 
		strcat(Names, "\n"); 
		(*Number)++; 
	} 
} 
/* 
* Print in a file in a neat fashion all the statistics we have collected 
* on an interface. 
*  MOS  
*  Input: 
                - F: File to output to.
		- InterfaceName: Interface name (e.g. eth0) 
		- Stats: Statistics collected.
	Output: 
		- None 
	 
*/ 
void DisplayStatFile(FILE* F, char*	InterfaceName, iwstats*	Stats) 
{ 
  iwrange	Range;
  int		HasRange = 0;
 
  /* Get range info if we can */
  if(get_range_info(SocketFileDescriptor, InterfaceName, &(Range)) >= 0)
    HasRange = 1;

  	if(Stats->qual.level != 0){ 
	  /*  If the statistics are in dBm */ 
  	if(Stats->qual.level > Range.max_qual.level) 
			fprintf(F, "Link quality:%d/%d  Signal level:%d dBm  Noise level:%d dBm\n", 
			Stats->qual.qual, Range.max_qual.qual, 
			Stats->qual.level - 0x100, 
			Stats->qual.noise - 0x100); 
		else 
		  /* Statistics are relative values (0 -> max) */ 
			fprintf(F, "Link quality:%d/%d  Signal level:%d/%d  Noise level:%d/%d\n", 
			Stats->qual.qual, Range.max_qual.qual, 
			Stats->qual.level, Range.max_qual.level, 
			Stats->qual.noise, Range.max_qual.noise); 
 
	} 
	else      
		/* We can't read the range, so we don't know... */ 
		fprintf(F, "Link quality:%d  Signal level:%d  Noise level:%d\n", 
		Stats->qual.qual, 
		Stats->qual.level, 
		Stats->qual.noise); 
		 
	fprintf(F," Rx invalid nwid:%d  invalid crypt:%d  invalid misc:%d\n", 
		Stats->discard.nwid, 
		Stats->discard.code, 
		Stats->discard.misc); 
} 

/* 
* Print on the screen in a neat fashion all the statistics we have collected 
* on an interface. 
*  MOS  
*  Input: 
		- InterfaceName: Interface name (e.g. eth0) 
		- Stats: Statistics collected.
	Output: 
		- None 
	
*/ 
void DisplayStat(char*	InterfaceName, iwstats*	Stats) 
{ 
  iwrange	Range;
  int		HasRange = 0;
 
  /* Get range info if we can */
  if(get_range_info(SocketFileDescriptor, InterfaceName, &(Range)) >= 0)
    HasRange = 1;

  	if(Stats->qual.level != 0){ 
	  /*  If the statistics are in dBm */ 
  	if(Stats->qual.level > Range.max_qual.level) 
			printf("Link quality:%d/%d  Signal level:%d dBm  Noise level:%d dBm\n", 
			Stats->qual.qual, Range.max_qual.qual, 
			Stats->qual.level - 0x100, 
			Stats->qual.noise - 0x100); 
		else 
		  /* Statistics are relative values (0 -> max) */ 
			printf("Link quality:%d/%d  Signal level:%d/%d  Noise level:%d/%d\n", 
			Stats->qual.qual, Range.max_qual.qual, 
			Stats->qual.level, Range.max_qual.level, 
			Stats->qual.noise, Range.max_qual.noise); 
 
	} 
	else      
		/* We can't read the range, so we don't know... */ 
		printf("Link quality:%d  Signal level:%d  Noise level:%d\n", 
		Stats->qual.qual, 
		Stats->qual.level, 
		Stats->qual.noise); 
		 
	printf("          Rx invalid nwid:%d  invalid crypt:%d  invalid misc:%d\n", 
		Stats->discard.nwid, 
		Stats->discard.code, 
		Stats->discard.misc); 
} 

/************************* SETTING ROUTINES **************************/

/*
 * Same as iwspy command line
 */
int
SetInfo(char *		InterfaceName,	/* Dev name */ 
	char *		Args[],	        /* Command line Args */
	int		Count)		/* Args Count */
{
	struct iwreq wrq;
	int	i;
	
	/* Set dev name */
	strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);
	
	/* if nothing after the device name */
	if(Count<1) ;
	  //iw_usage();
	
	/* The other Args on the line specify options to be set... */
	for(i = 0; i < Count; i++)
    {
		/* ---------- Set network ID ---------- */
		if((!strcasecmp(Args[i], "nwid")) ||
			(!strcasecmp(Args[i], "domain")))
		{
			i++;
			if(i >= Count) ;
				//iw_usage();
			if((!strcasecmp(Args[i], "off")) ||
				(!strcasecmp(Args[i], "any")))
				wrq.u.nwid.disabled = 1;
			else
				if(!strcasecmp(Args[i], "on"))
				{
					/* Get old nwid */
					if(ioctl(SocketFileDescriptor, SIOCGIWNWID, &wrq) < 0)
					{
						fprintf(stderr, "SIOCGIWNWID: %s\n", strerror(errno));
						return(-1);
					}
					strcpy(wrq.ifr_name, InterfaceName);
					wrq.u.nwid.disabled = 0;
				}
				else
					if(sscanf(Args[i], "%lX", (unsigned long *) &(wrq.u.nwid.value))
					   != 1) ;
			// iw_usage();
					else
						wrq.u.nwid.disabled = 0;
					wrq.u.nwid.fixed = 1;
					
					if(ioctl(SocketFileDescriptor, SIOCSIWNWID, &wrq) < 0)
					{
						fprintf(stderr, "SIOCSIWNWID: %s\n", strerror(errno));
						return(-1);
					}
					continue;
		}
		
		/* ---------- Set frequency / channel ---------- */
		if((!strncmp(Args[i], "freq", 4)) ||
			(!strcmp(Args[i], "channel")))
		{
			double		freq;
			
			if(++i >= Count) ;
				// iw_usage();
			if(sscanf(Args[i], "%lg", &(freq)) != 1) ;
				// iw_usage();
			if(index(Args[i], 'G')) freq *= GIGA;
			if(index(Args[i], 'M')) freq *= MEGA;
			if(index(Args[i], 'k')) freq *= KILO;
			
			float2freq(freq, &(wrq.u.freq));
			
			if(ioctl(SocketFileDescriptor, SIOCSIWFREQ, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWFREQ: %s\n", strerror(errno));
				return(-1);
			}
			continue;
		}
		
		/* ---------- Set sensitivity ---------- */
		if(!strncmp(Args[i], "sens", 4))
		{
		  if(++i >= Count) ;
		  //				iw_usage();
		  if(sscanf(Args[i], "%d", &(wrq.u.sens.value)) != 1);
				// iw_usage();
			
			if(ioctl(SocketFileDescriptor, SIOCSIWSENS, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWSENS: %s\n", strerror(errno));
				return(-1);
			}
			continue;
		}
		
		/* ---------- Set encryption stuff ---------- */
		if((!strncmp(Args[i], "enc", 3)) ||
			(!strcmp(Args[i], "key")))
		{
			unsigned char	key[IW_ENCODING_TOKEN_MAX];
			
			if(++i >= Count) ;
				//iw_usage();
			
			if(!strcasecmp(Args[i], "on"))
			{
				/* Get old encryption information */
				wrq.u.data.pointer = (caddr_t) key;
				wrq.u.data.length = 0;
				wrq.u.data.flags = 0;
				if(ioctl(SocketFileDescriptor, SIOCGIWENCODE, &wrq) < 0)
				{
					fprintf(stderr, "SIOCGIWENCODE: %s\n", strerror(errno));
					return(-1);
				}
				strcpy(wrq.ifr_name, InterfaceName);
				wrq.u.data.flags &= ~IW_ENCODE_DISABLED;	/* Enable */
			}
			else
			{
				char *	buff;
				char *	p;
				int		temp;
				int		k = 0;
				int		gotone = 1;
				
				wrq.u.data.pointer = (caddr_t) NULL;
				wrq.u.data.flags = 0;
				wrq.u.data.length = 0;
				
				/* -- Check for the key -- */
				if(!strncmp(Args[i], "s:", 2))
				{
					/* First case : as an ASCII string */
					wrq.u.data.length = strlen(Args[i] + 2);
					if(wrq.u.data.length > IW_ENCODING_TOKEN_MAX)
						wrq.u.data.length = IW_ENCODING_TOKEN_MAX;
					strncpy(key, Args[i] + 2, wrq.u.data.length);
					wrq.u.data.pointer = (caddr_t) key;
					++i;
					gotone = 1;
				}
				else
				{
					/* Second case : has hexadecimal digits */
					p = buff = malloc(strlen(Args[i]) + 1);
					strcpy(buff, Args[i]);
					
					p = strtok(buff, "-:;.,");
					while(p != (char *) NULL)
					{
						if(sscanf(p, "%2X", &temp) != 1)
						{
							gotone = 0;
							break;
						}
						key[k++] = (unsigned char) (temp & 0xFF);
						if(strlen(p) > 2)	/* Token not finished yet */
							p += 2;
						else
							p = strtok((char *) NULL, "-:;.,");
					}
					free(buff);
					
					if(gotone)
					{
						++i;
						wrq.u.data.length = k;
						wrq.u.data.pointer = (caddr_t) key;
					}
				}
				
				/* -- Check for token index -- */
				if((i < Count) &&
					(sscanf(Args[i], "[%d]", &temp) == 1) &&
					(temp > 0) && (temp < IW_ENCODE_INDEX))
				{
					wrq.u.encoding.flags |= temp;
					++i;
					gotone = 1;
				}
				
				/* -- Check the various flags -- */
				if(i < Count)
				{
					if(!strcasecmp(Args[i], "off"))
						wrq.u.data.flags |= IW_ENCODE_DISABLED;
					if(!strcasecmp(Args[i], "open"))
						wrq.u.data.flags |= IW_ENCODE_OPEN;
					if(!strncasecmp(Args[i], "restricted", 5))
						wrq.u.data.flags |= IW_ENCODE_RESTRICTED;
					if(wrq.u.data.flags & IW_ENCODE_FLAGS)
					{
						++i;
						gotone = 1;
					}
				}
				
				if(!gotone) ;
				//iw_usage();
				--i;
			}
			
			if(ioctl(SocketFileDescriptor, SIOCSIWENCODE, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWENCODE(%d): %s\n",
					errno, strerror(errno));
				return(-1);
			}
			continue;
	}
	
	/* ---------- Set ESSID ---------- */
	if(!strcasecmp(Args[i], "essid"))
	{
		char		essid[IW_ESSID_MAX_SIZE + 1];
		
		i++;
		if(i >= Count) ;
		// iw_usage();
		if((!strcasecmp(Args[i], "off")) ||
			(!strcasecmp(Args[i], "any")))
		{
			wrq.u.essid.flags = 0;
			essid[0] = '\0';
		}
		else
			if(!strcasecmp(Args[i], "on"))
			{
				/* Get old essid */
				wrq.u.essid.pointer = (caddr_t) essid;
				wrq.u.essid.length = 0;
				wrq.u.essid.flags = 0;
				if(ioctl(SocketFileDescriptor, SIOCGIWESSID, &wrq) < 0)
				{
					fprintf(stderr, "SIOCGIWESSID: %s\n", strerror(errno));
					return(-1);
				}
				strcpy(wrq.ifr_name, InterfaceName);
				wrq.u.essid.flags = 1;
			}
			else
				if(strlen(Args[i]) > IW_ESSID_MAX_SIZE)
				{
					fprintf(stderr, "ESSID too long (max %d): ``%s''\n",
						IW_ESSID_MAX_SIZE, Args[i]);
					//iw_usage();
				}
				else
				{
					wrq.u.essid.flags = 1;
					strcpy(essid, Args[i]);
				}
				
				wrq.u.essid.pointer = (caddr_t) essid;
				wrq.u.essid.length = strlen(essid) + 1;
				if(ioctl(SocketFileDescriptor, SIOCSIWESSID, &wrq) < 0)
				{
					fprintf(stderr, "SIOCSIWESSID: %s\n", strerror(errno));
					return(-1);
				}
				continue;
	}
	
	/* ---------- Set AP address ---------- */
	if(!strcasecmp(Args[i], "ap"))
	{
	  if(++i >= Count);
	  //iw_usage();
		
		/* Check if we have valid address types */
		if(check_addr_type(SocketFileDescriptor, InterfaceName) < 0)
		{
			fprintf(stderr, "%-8.8s  Interface doesn't support MAC & IP addresses\n", InterfaceName);
			return(-1);
		}
		
		/* Get the address */
		if(in_addr(SocketFileDescriptor, InterfaceName, Args[i++], &(wrq.u.ap_addr)) < 0)
		  ;//iw_usage();
		
		if(ioctl(SocketFileDescriptor, SIOCSIWAP, &wrq) < 0)
		{
			fprintf(stderr, "SIOCSIWAP: %s\n", strerror(errno));
			return(-1);
		}
		continue;
	}
	
	/* ---------- Set NickName ---------- */
	if(!strncmp(Args[i], "nick", 4))
	{
		i++;
		if(i >= Count)
		  ;//iw_usage();
		if(strlen(Args[i]) > IW_ESSID_MAX_SIZE)
		{
			fprintf(stderr, "Name too long (max %d) : ``%s''\n",
				IW_ESSID_MAX_SIZE, Args[i]);
			//iw_usage();
		}
		
		wrq.u.essid.pointer = (caddr_t) Args[i];
		wrq.u.essid.length = strlen(Args[i]) + 1;
		if(ioctl(SocketFileDescriptor, SIOCSIWNICKN, &wrq) < 0)
		{
			fprintf(stderr, "SIOCSIWNICKN: %s\n", strerror(errno));
			return(-1);
		}
		continue;
	}
	
	/* ---------- Set Bit-Rate ---------- */
	if((!strncmp(Args[i], "bit", 3)) ||
		(!strcmp(Args[i], "rate")))
	{
		if(++i >= Count)
		  ;//iw_usage();
		if(!strcasecmp(Args[i], "auto"))
		{
			wrq.u.bitrate.value = -1;
			wrq.u.bitrate.fixed = 0;
		}
		else
		{
			if(!strcasecmp(Args[i], "fixed"))
			{
				/* Get old bitrate */
				if(ioctl(SocketFileDescriptor, SIOCGIWRATE, &wrq) < 0)
				{
					fprintf(stderr, "SIOCGIWRATE: %s\n", strerror(errno));
					return(-1);
				}
				strcpy(wrq.ifr_name, InterfaceName);
				wrq.u.bitrate.fixed = 1;
			}
			else			/* Should be a numeric value */
			{
				double		brate;
				
				if(sscanf(Args[i], "%lg", &(brate)) != 1)
				  ;//iw_usage();
				if(index(Args[i], 'G')) brate *= GIGA;
				if(index(Args[i], 'M')) brate *= MEGA;
				if(index(Args[i], 'k')) brate *= KILO;
				wrq.u.bitrate.value = (long) brate;
				wrq.u.bitrate.fixed = 1;
				
				/* Check for an additional argument */
				if(((i+1) < Count) &&
					(!strcasecmp(Args[i+1], "auto")))
				{
					wrq.u.bitrate.fixed = 0;
					++i;
				}
			}
		}
		
		if(ioctl(SocketFileDescriptor, SIOCSIWRATE, &wrq) < 0)
		{
			fprintf(stderr, "SIOCSIWRATE: %s\n", strerror(errno));
			return(-1);
		}
		continue;
	}
	
	/* ---------- Set RTS threshold ---------- */
	if(!strncasecmp(Args[i], "rts", 3))
	{
		i++;
		if(i >= Count)
		  ;//iw_usage();
		wrq.u.rts.value = -1;
		wrq.u.rts.fixed = 1;
		wrq.u.rts.disabled = 0;
		if(!strcasecmp(Args[i], "off"))
			wrq.u.rts.disabled = 1;	/* i.e. max size */
		else
			if(!strcasecmp(Args[i], "auto"))
				wrq.u.rts.fixed = 0;
			else
			{
				if(!strcasecmp(Args[i], "fixed"))
				{
					/* Get old RTS threshold */
					if(ioctl(SocketFileDescriptor, SIOCGIWRTS, &wrq) < 0)
					{
						fprintf(stderr, "SIOCGIWRTS: %s\n", strerror(errno));
						return(-1);
					}
					strcpy(wrq.ifr_name, InterfaceName);
					wrq.u.rts.fixed = 1;
				}
				else			/* Should be a numeric value */
					if(sscanf(Args[i], "%ld", (unsigned long *) &(wrq.u.rts.value))
						!= 1)
					  ;//iw_usage();
			}
			
			if(ioctl(SocketFileDescriptor, SIOCSIWRTS, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWRTS: %s\n", strerror(errno));
				return(-1);
			}
			continue;
	}
	
	/* ---------- Set fragmentation threshold ---------- */
	if(!strncmp(Args[i], "frag", 4))
	{
		i++;
		if(i >= Count)
		  ;//iw_usage();
		wrq.u.frag.value = -1;
		wrq.u.frag.fixed = 1;
		wrq.u.frag.disabled = 0;
		if(!strcasecmp(Args[i], "off"))
			wrq.u.frag.disabled = 1;	/* i.e. max size */
		else
			if(!strcasecmp(Args[i], "auto"))
				wrq.u.frag.fixed = 0;
			else
			{
				if(!strcasecmp(Args[i], "fixed"))
				{
					/* Get old fragmentation threshold */
					if(ioctl(SocketFileDescriptor, SIOCGIWFRAG, &wrq) < 0)
					{
						fprintf(stderr, "SIOCGIWFRAG: %s\n", strerror(errno));
						return(-1);
					}
					strcpy(wrq.ifr_name, InterfaceName);
					wrq.u.frag.fixed = 1;
				}
				else			/* Should be a numeric value */
					if(sscanf(Args[i], "%ld", (unsigned long *) &(wrq.u.frag.value))
						!= 1)
					  ;//iw_usage();
			}
			
			if(ioctl(SocketFileDescriptor, SIOCSIWFRAG, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWFRAG: %s\n", strerror(errno));
				return(-1);
			}
			continue;
	}
	
	/* ---------- Set operation mode ---------- */
	if(!strcmp(Args[i], "mode"))
	{
		int	k;
		
		i++;
		if(i >= Count)
		  ;//iw_usage();
		
		if(sscanf(Args[i], "%d", &k) != 1)
		{
			k = 0;
			while(k < 6 && strncasecmp(Args[i], OperationMode[k], 3))
				k++;
		}
		if((k > 5) || (k < 0))
		  ;//iw_usage();
		
		wrq.u.mode = k;
		if(ioctl(SocketFileDescriptor, SIOCSIWMODE, &wrq) < 0)
		{
			fprintf(stderr, "SIOCSIWMODE: %s\n", strerror(errno));
			return(-1);
		}
		continue;
	}
	
	/* ---------- Set Power Management ---------- */
	if(!strncmp(Args[i], "power", 3))
	{
		if(++i >= Count)
		  ;//iw_usage();
		
		if(!strcasecmp(Args[i], "off"))
			wrq.u.power.disabled = 1;	/* i.e. max size */
		else
			if(!strcasecmp(Args[i], "on"))
			{
				/* Get old Power Info */
				if(ioctl(SocketFileDescriptor, SIOCGIWPOWER, &wrq) < 0)
				{
					fprintf(stderr, "SIOCGIWFRAG: %s\n", strerror(errno));
					return(-1);
				}
				strcpy(wrq.ifr_name, InterfaceName);
				wrq.u.power.disabled = 0;
			}
			else
			{
				double		temp;
				int		gotone = 0;
				/* Default - nope */
				wrq.u.power.flags = IW_POWER_ON;
				wrq.u.power.disabled = 0;
				
				/* Check value modifier */
				if(!strcasecmp(Args[i], "period"))
				{
					wrq.u.power.flags = IW_POWER_PERIOD;
					if(++i >= Count)
					  ;//iw_usage();
				}
				else
					if(!strcasecmp(Args[i], "timeout"))
					{
						wrq.u.power.flags = IW_POWER_TIMEOUT;
						if(++i >= Count)
						  ;//iw_usage();
					}
					
					/* Is there any value to grab ? */
					if(sscanf(Args[i], "%lg", &(temp)) == 1)
					{
						temp *= MEGA;	/* default = s */
						if(index(Args[i], 'u')) temp /= MEGA;
						if(index(Args[i], 'm')) temp /= KILO;
						wrq.u.power.value = (long) temp;
						if(wrq.u.power.flags == IW_POWER_ON)
							wrq.u.power.flags = IW_POWER_PERIOD;
						++i;
						gotone = 1;
					}
					
					/* Now, check the mode */
					if(i < Count)
					{
						if(!strcasecmp(Args[i], "all"))
							wrq.u.power.flags |= IW_POWER_ALL_R;
						if(!strncasecmp(Args[i], "unicast", 4))
							wrq.u.power.flags |= IW_POWER_UNICAST_R;
						if(!strncasecmp(Args[i], "multicast", 5))
							wrq.u.power.flags |= IW_POWER_MULTICAST_R;
						if(!strncasecmp(Args[i], "force", 5))
							wrq.u.power.flags |= IW_POWER_FORCE_S;
						if(!strcasecmp(Args[i], "repeat"))
							wrq.u.power.flags |= IW_POWER_REPEATER;
						if(wrq.u.power.flags & IW_POWER_MODE)
						{
							++i;
							gotone = 1;
						}
					}
					if(!gotone)
					  ;//iw_usage();
					--i;
			}
			
			if(ioctl(SocketFileDescriptor, SIOCSIWPOWER, &wrq) < 0)
			{
				fprintf(stderr, "SIOCSIWPOWER(%d): %s\n",
					errno, strerror(errno));
				return(-1);
			}
			continue;
	}
	
	/* ---------- Other ---------- */
	/* Here we have an unrecognised arg... */
	fprintf(stderr, "Invalid argument : %s\n", Args[i]);
	;//iw_usage();
	return(-1);
    }		/* for(index ... */
	return(0);
}
 
/* 
* MOS 
* Opens a socket for talking with the networking kernel 
*/
int OpenSocket() 
{ 
  printf("Wireless Extentions API\n  Copyright (c) 2001 By Moustafa A. Youssef- MIND Lab\nBased on\n");
  printf("Wireless Tools By Jean II - HPLB '99\n");
  if((SocketFileDescriptor = sockets_open()) < 0){ 
    perror("socket"); 
    return -1; 
  } 
  return 0; 
} 
 
void CloseSocket() 
{ 
	close(SocketFileDescriptor); 
} 

/**********************************************************************************/
/*****            Spy Subroutines                                              ****/
/**********************************************************************************/
struct sockaddr RegisteredAddresses[IW_MAX_SPY];
int NRegistered;

/*------------------------------------------------------------------*/
/*
 *  MOS
 * Add an address to the list of addresses
 *Input:
       - Address: MAC or IP address 
       - InterfaceName: Interface Name;
  Output:
       - -1 if error
*/
int RegisterAddress(char * Address,
	     char * InterfaceName)		/* Dev name */
{
  struct iwreq		wrq;
  char	Buffer[(sizeof(struct iw_quality) +
		sizeof(struct sockaddr)) * IW_MAX_SPY];


  /* Check if we have valid address types */
  if(check_addr_type(SocketFileDescriptor, InterfaceName) < 0)
    {
      fprintf(stderr, "%-8.8s  Interface doesn't support MAC & IP addresses\n", InterfaceName);
      return(-1);
    }

  /* add all addresses already in the driver */

      strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);
      wrq.u.data.pointer = (caddr_t) Buffer;
      wrq.u.data.length = 0;
      wrq.u.data.flags = 0;
      if(ioctl(SocketFileDescriptor, SIOCGIWSPY, &wrq) < 0)
	{
	  fprintf(stderr, "Interface doesn't accept reading addresses...\n");
	  fprintf(stderr, "SIOCGIWSPY: %s\n", strerror(errno));
	  return(-1);
	}

      /* Copy old addresses */
      NRegistered = wrq.u.data.length;
      memcpy(RegisteredAddresses, Buffer, NRegistered * sizeof(struct sockaddr));

  /* Read other args on command line */
  if (NRegistered < IW_MAX_SPY)
    {
      if(in_addr(SocketFileDescriptor, InterfaceName, Address, &(RegisteredAddresses[NRegistered])) < 0)
	return (-1);
      //printf("%ld\n", RegisteredAddresses[NRegistered].sa_data);
      NRegistered++;
    }
else {
    fprintf(stderr, "Maximium number of registered addresses (%d) reached\n", IW_MAX_SPY);
    return (-1);
}

  /* Check the number of addresses */
   /* Time to do send addresses to the driver */
  strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);
  wrq.u.data.pointer = (caddr_t) RegisteredAddresses;
  wrq.u.data.length = NRegistered;
  wrq.u.data.flags = 0;
  if(ioctl(SocketFileDescriptor, SIOCSIWSPY, &wrq) < 0)
    {
      fprintf(stderr, "Interface doesn't accept addresses...\n");
      fprintf(stderr, "SIOCSIWSPY: %s\n", strerror(errno));
      return(-1);
    }

  return(0);
}

void Initialize()
{
  NRegistered= 0;
}

/*------------------------------------------------------------------*/
/*
 * Display the spy list of addresses and the associated stats
 * Input:
       - InterfaceName: Interface Name
   Output:
       - Address: List of Addresses
       - Stats: List of statistics
       - returns number of addreses in teh spy list, -1 if error
 */
int GetAddressStats(char *  InterfaceName, char* Address, struct iw_quality* Stats)
{
  struct iwreq		wrq;
  char		Buffer[(sizeof(struct iw_quality) +
			sizeof(struct sockaddr)) * IW_MAX_SPY];
  struct sockaddr 	HWA[IW_MAX_SPY];
  struct iw_quality 	Qual[IW_MAX_SPY];
  iwrange	Range;
  int		HasRange = 0;
  int		n;
  int		i;

  /* Collect stats */
  strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);
  wrq.u.data.pointer = (caddr_t) Buffer;
  wrq.u.data.length = 0;
  wrq.u.data.flags = 0;
  if(ioctl(SocketFileDescriptor, SIOCGIWSPY, &wrq) < 0)
    {
      fprintf(stderr, "%-8.8s  Interface doesn't support wireless statistic collection\n\n", InterfaceName);
      return (-1);
    }

  /* Number of addresses */
  n = wrq.u.data.length;



  /* Check if we have valid address types */
  if(check_addr_type(SocketFileDescriptor, InterfaceName) < 0)
    {
      fprintf(stderr, "%-8.8s  Interface doesn't support MAC & IP addresses\n\n", InterfaceName);
      return (-1);
    }

  /* if(in_addr(SocketFileDescriptor, InterfaceName, Address, &BinAddress) < 0)
	return (-1);
 printf("%d\n", BinAddress.sa_data);
 printf("%d\n", BinAddress.sa_data);
  */
   /* Get range info if we can */
  if(get_range_info(SocketFileDescriptor, InterfaceName, &(Range)) >= 0)
    HasRange = 1;

  /* Display it */
  if(n == 0){
    printf("%-8.8s  No statistics to collect\n", InterfaceName);
    return (-1);
   }
    
  /* The two lists */

  memcpy(HWA, Buffer, n * sizeof(struct sockaddr));
  memcpy(Qual, Buffer + n*sizeof(struct sockaddr), n*sizeof(struct iw_quality));

  for(i = 0; i < n; i++)
    {
      printf("%s %s\n", Address, pr_ether(HWA[i].sa_data));
      if (!strcmp(Address, pr_ether(HWA[i].sa_data))){
	Stats->qual= Qual[i].qual;
	Stats->level= Qual[i].level;
	Stats->noise= Qual[i].noise;
	Stats->updated= Qual[i].updated;
	return 0;
      }
      /*
      if(HasRange && (Qual[i].level != 0))
	// If the statistics are in dBm 
	if(Qual[i].level > range.max_Qual.level)
	  printf("    %s : Quality %d/%d ; Signal %d dBm ; Noise %d dBm %s\n",
		 pr_ether(HWA[i].sa_data),
		 Qual[i].qual, range.max_Qual.Qual,
		 Qual[i].level - 0x100, Qual[i].noise - 0x100,
		 Qual[i].updated & 0x7 ? "(updated)" : "");
	else
	  printf("    %s : Quality %d/%d ; Signal %d/%d ; Noise %d/%d %s\n",
		 pr_ether(HWA[i].sa_data),
		 Qual[i].qual, range.max_Qual.Qual,
		 Qual[i].level, range.max_Qual.level,
		 Qual[i].noise, range.max_Qual.noise,
		 Qual[i].updated & 0x7 ? "(updated)" : "");
      else
	printf("    %s : Quality %d ; Signal %d ; Noise %d %s\n",
	       pr_ether(HWA[i].sa_data),
	       Qual[i].Qual, qual[i].level, Qual[i].noise,
	       Qual[i].updated & 0x7 ? "(updated)" : "");
      */
    }
  printf("Address not found\n");
return (-1); // address not found 
}
///////////////////////////
/*
 *  Get all the access points information
 *  Returns number of AP found, -1 if error
 * Input:
       - InterfaceName: Interface Name
   Output:
       - Address: List of Addresses
       - Qual: List of statistics
       - returns number of addreses in teh spy list, -1 if error
 */
int GetAllAPInformation(char* InterfaceName,  struct sockaddr * Address, struct iw_quality* Qual, int* HasQual)
{
  struct iwreq		wrq;
  char	Buffer[(sizeof(struct iw_quality)+ sizeof(struct sockaddr)) * IW_MAX_AP];
 
  /* Collect stats */
  strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);
  wrq.u.data.pointer = (caddr_t) Buffer;
  wrq.u.data.length = 0;
  wrq.u.data.flags = 0;
  if(ioctl(SocketFileDescriptor, SIOCGIWAPLIST, &wrq) < 0)
    {
      fprintf(stderr, "%-8.8s  Interface doesn't have a list of Access Points\n\n", InterfaceName);
      return -1;
    }

  memcpy(Address, (char *) Buffer,sizeof(struct sockaddr) *  wrq.u.data.length);
  *HasQual= wrq.u.data.flags;
  if (wrq.u.data.flags) 
    memcpy(Qual,  (char*) (Buffer + (sizeof(struct sockaddr) *  wrq.u.data.length)), sizeof(struct iw_quality) *  wrq.u.data.length);

  return wrq.u.data.length; 
}


/*------------------------------------------------------------------*/
/*
 * Display the list of ap addresses and the associated stats
 * Exacly the same as the spy list, only with different IOCTL and messages
 */
void DisplayAllAPInfo(char * InterfaceName,  int n, struct sockaddr * Address, struct iw_quality* Qual, int HasQual)
{
  struct sockaddr * HWA;
  iwrange	Range;
  int		HasRange = 0;
  int		i;

  /* The two lists */
  HWA = Address;

  /* Check if we have valid address types */
  if(check_addr_type(SocketFileDescriptor, InterfaceName) < 0)
    {
      fprintf(stderr, "%-8.8s  Interface doesn't support MAC & IP addresses\n\n", InterfaceName);
      return;
    }

  /* Get range info if we can */
  if(get_range_info(SocketFileDescriptor, InterfaceName, &(Range)) >= 0)
    HasRange = 1;

  /* Display it */
  if(n == 0)
    printf("%-8.8s  No Access Point in range\n", InterfaceName);
  else
    printf("%-8.8s  Access Points in range:\n", InterfaceName);
  for(i = 0; i < n; i++)
    {
      if(HasQual)
	if(HasRange)
	  /* If the statistics are in dBm */
	  if(Qual[i].level > Range.max_qual.level)
	    printf("    %s : Quality %d/%d ; Signal %d dBm ; Noise %d dBm %s\n",
		   pr_ether(HWA[i].sa_data),
		   Qual[i].qual, Range.max_qual.qual,
		   Qual[i].level - 0x100, Qual[i].noise - 0x100,
		   Qual[i].updated & 0x7 ? "(updated)" : "");
	  else
	    printf("    %s : Quality %d/%d ; Signal %d/%d ; Noise %d/%d %s\n",
		   pr_ether(HWA[i].sa_data),
		   Qual[i].qual, Range.max_qual.qual,
		   Qual[i].level, Range.max_qual.level,
		   Qual[i].noise, Range.max_qual.noise,
		   Qual[i].updated & 0x7 ? "(updated)" : "");
	else
	  printf("    %s : Quality %d ; Signal %d ; Noise %d %s\n",
		 pr_ether(HWA[i].sa_data),
		 Qual[i].qual, Qual[i].level, Qual[i].noise, 
		 Qual[i].updated & 0x7 ? "(updated)" : "");
      else
	printf("    %s\n", pr_ether(HWA[i].sa_data));
    }
  printf("\n");
}

//////////////////////////////////////////////
//////////////////////////////////////////////
/*
 *  Issues a private command to the driver
 */
int PrivateCommand(char * InterfaceName,        /* Dev name */
                   char * Args[],		/* Command line args */
		   int	  Count)		/* Args count */
{
  u_char	Buffer[1024];
  struct iwreq	wrq;
  int		i = 0;		/* Start with first arg */
  int		k;
  iwprivargs	Priv[16];
  int		Number;

  /* Read the private ioctls */
  Number = get_priv_info(SocketFileDescriptor, InterfaceName, Priv);

  /* Is there any ? */
  if(Number <= 0)
    {
      /* Could skip this message ? */
      fprintf(stderr, "%-8.8s  no private ioctls.\n\n", InterfaceName);
      return(-1);
    }

  /* Search the correct ioctl */
  k = -1;
  while((++k < Number) && strcmp(Priv[k].name, Args[i]));

  /* If not found... */
  if(k == Number)
    {
      fprintf(stderr, "Invalid command : %s\n", Args[i]);
      return(-1);
    }
	  
  /* Next arg */
  i++;

  /* If we have to set some data */
  if((Priv[k].set_args & IW_PRIV_TYPE_MASK) &&
     (Priv[k].set_args & IW_PRIV_SIZE_MASK))
    {
      switch(Priv[k].set_args & IW_PRIV_TYPE_MASK)
	{
	case IW_PRIV_TYPE_BYTE:
	  /* Number of args to fetch */
	  wrq.u.data.length = Count - 1;
	  if(wrq.u.data.length > (Priv[k].set_args & IW_PRIV_SIZE_MASK))
	    wrq.u.data.length = Priv[k].set_args & IW_PRIV_SIZE_MASK;

	  /* Fetch args */
	  for(; i < wrq.u.data.length + 1; i++)
	    sscanf(Args[i], "%d", (int *)(Buffer + i - 1));
	  break;

	case IW_PRIV_TYPE_INT:
	  /* Number of args to fetch */
	  wrq.u.data.length = Count - 1;
	  if(wrq.u.data.length > (Priv[k].set_args & IW_PRIV_SIZE_MASK))
	    wrq.u.data.length = Priv[k].set_args & IW_PRIV_SIZE_MASK;

	  /* Fetch args */
	  for(; i < wrq.u.data.length + 1; i++)
	    sscanf(Args[i], "%d", ((u_int *) Buffer) + i - 1);
	  break;

	case IW_PRIV_TYPE_CHAR:
	  if(i < Count)
	    {
	      /* Size of the string to fetch */
	      wrq.u.data.length = strlen(Args[i]) + 1;
	      if(wrq.u.data.length > (Priv[k].set_args & IW_PRIV_SIZE_MASK))
		wrq.u.data.length = Priv[k].set_args & IW_PRIV_SIZE_MASK;

	      /* Fetch string */
	      memcpy(Buffer, Args[i], wrq.u.data.length);
	      Buffer[sizeof(Buffer) - 1] = '\0';
	      i++;
	    }
	  else
	    {
	      wrq.u.data.length = 1;
	      Buffer[0] = '\0';
	    }
	  break;

	default:
	  fprintf(stderr, "Not yet implemented...\n");
	  return(-1);
	}
	  
      if((Priv[k].set_args & IW_PRIV_SIZE_FIXED) &&
	 (wrq.u.data.length != (Priv[k].set_args & IW_PRIV_SIZE_MASK)))
	{
	  printf("The command %s need exactly %d argument...\n",
		 Priv[k].name, Priv[k].set_args & IW_PRIV_SIZE_MASK);
	  return(-1);
	}
    }	/* if args to set */
  else
    {
      wrq.u.data.length = 0L;
    }

  strncpy(wrq.ifr_name, InterfaceName, IFNAMSIZ);

  if((Priv[k].set_args & IW_PRIV_SIZE_FIXED) &&
     (byte_size(Priv[k].set_args) < IFNAMSIZ))
    memcpy(wrq.u.name, Buffer, IFNAMSIZ);
  else
    {
      wrq.u.data.pointer = (caddr_t) Buffer;
      wrq.u.data.flags = 0;
    }

  /* Perform the private ioctl */
  if(ioctl(SocketFileDescriptor, Priv[k].cmd, &wrq) < 0)
    {
      fprintf(stderr, "Interface doesn't accept private ioctl...\n");
      fprintf(stderr, "%X: %s\n", Priv[k].cmd, strerror(errno));
      return(-1);
    }

  /* If we have to get some data */
  if((Priv[k].get_args & IW_PRIV_TYPE_MASK) &&
     (Priv[k].get_args & IW_PRIV_SIZE_MASK))
    {
      int	j;
      int	n = 0;		/* Number of args */

      printf("%-8.8s  %s:", InterfaceName, Priv[k].name);

      if((Priv[k].get_args & IW_PRIV_SIZE_FIXED) &&
	 (byte_size(Priv[k].get_args) < IFNAMSIZ))
	{
	  memcpy(Buffer, wrq.u.name, IFNAMSIZ);
	  n = Priv[k].get_args & IW_PRIV_SIZE_MASK;
	}
      else
	n = wrq.u.data.length;

      switch(Priv[k].get_args & IW_PRIV_TYPE_MASK)
	{
	case IW_PRIV_TYPE_BYTE:
	  /* Display args */
	  for(j = 0; j < n; j++)
	    printf("%d  ", Buffer[j]);
	  printf("\n");
	  break;

	case IW_PRIV_TYPE_INT:
	  /* Display args */
	  for(j = 0; j < n; j++)
	    printf("%d  ", ((u_int *) Buffer)[i]);
	  printf("\n");
	  break;

	case IW_PRIV_TYPE_CHAR:
	  /* Display args */
	  Buffer[wrq.u.data.length - 1] = '\0';
	  printf("%s\n", Buffer);
	  break;

	default:
	  fprintf(stderr, "Not yet implemented...\n");
	  return(-1);
	}
    }	/* if args to set */

  return(0);
}









