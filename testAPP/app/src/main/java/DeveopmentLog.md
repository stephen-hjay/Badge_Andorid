Use SingleTask launch mode to avoid abnormal exit with fractions in stack
and avoid page jump recreate page instance
page2 singleTask still recreate page
singleInstance is ok

Use 
setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//force portrait
in OnCreate to force protrait

Global variable and classes can be static variable and class in a normal class


without creating the page cannot get the components
solution: 
1. create all pages on first create and global flag
	Use startActivityForResult and OnActivityResult for communication between activities or just set in 2nd page
2. use flag to see whether created, if have ,set sensor register
3. startSensor decide whether float[] is null, register or not
	2nd page starts and get components send to Sensors
	Sensor float static?
	
	
When finish or jump page in onCreate, the instance is not created.

use interface for calls between sensor and activity
tansfer a inner class() with interface into sensor to call

scan function need a single activity to start
use another interface to stop it and sleep for some time to get back to state machine

Virtual Machine doesn't support bluetooth
need phones to test and debug
use a class extends Activity to start bluetooth, but not used as an Activity


