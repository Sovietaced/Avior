Avior v1.2
==========
Avior is a network management tool designed to make floodlight network administration and testing easier. Avior supplies
a conventient user interface that alleviates the user's dependency on using python scripts and analyzing JSON data.

Features
--------
* Static flow entry pusher user interface. Add, modify, and delete flows.
* Useful error checking and flow verification. 
* Detailed controller, switch, device, port, and flow statistics updating in real time.
* Easy to use Patch Panel.

Compiling
---------

In order to compile Avior you will need to do a couple of things.

1. Clone the repository
2. Import the project into eclipse
3. Configure your build path to include the appropriate SWT jar for your operating system. 
By default the 64 bit Linux jar is included. If you are using Windows or Mac, you will need to remove this and add the appropriate one
from the Lib folder.

Compatability 
--------------
* 64/32 bit : Linux, Windows, OSX

Requirements
------------
* 64 bit Java v1.6
* Network connection to a Floodlight controller running v.85+

FAQ
---
* I am getting a thread error on OSX, what do?
Please use the shell script packaged with the OSX download.

Changelog
---------

v1.2
* Added port, flow statistics for each switch
* Added error checking for actions, match, etc. Checks will actually scan the switch for valid ports. 
* Flow verification added since the static flow pusher does not adequately represent flows on a switch.
* Simple patch panel added, 5 easy clicks to add a logical patch.
* Massive bug fixes, code cleansin and optimization.

v1.1
* Secondary release, module removed from the floodlight controller and built as a separate application. 
* Use of restAPI implemented, more network information, more stable.
* Bug fixes.

v1.0
* Initial release, application built into the floodlight controller as a module.

About
-----

Avior is developed by Jason Parraga, with help from Ryan Flaherty, for the Marist Openflow Research project. To report bugs, post suggestions, or any other inquiries, please visit http://openflow.marist.edu/

Floodlight is an Apache licensed, Java based OpenFlow controller originally written by David Erickson at Stanford University.

