

I use this Android app to play with ideas. A friend said it could be helpful to people who have lost the ability to speak, Stroke etc, It can be operated with one finger and can speak any number of words phases etc as long as the person or a friend adds them. 

Just by scooting about the screen[s] with a finger the words are spoken (Only English at the moment!). It is untried!

Hope it helps and I am interested in positive suggestions but time constraints limit my ability to react or support the app! 

The forms and data are stored in SQLite. The database with '.app' extension holds the forms the second database with a '.db' extension holds the data. When you backup, copies of the databases are placed on the SD card. The databases can be copied to other Android devices with YASFA installed and restored. A new installation of YASFA copies the default databases from assets to the databases directory.

As previously mentioned I use this app to play with ideas so there are a number of issues I simply haven’t addressed.  When you create a new control which could have data associated with it, it is assigned a GUID this is used as the column name in the data database against the given form. If you subsequently delete the control the column is orphaned so if you keep adding and deleting controls it will lead to a table with lots of orphaned columns. This only applies to data controls Edit Text, Slider, Radio Buttons etc. not static controls like Labels / Say It etc.

Blobs: camera, drawing, picture items; are stored at a low resolution to accommodate size limitations of the Android interface to SQLite. Similarly sound recordings cannot be over long.

The picture control only looks for items with a ‘.jpg’ extension on the SD card when importing images.  Adding an Image to a button only looks for items with a ‘.png’ extension. There's no special reason for this; it was just convenient for me.
I only implemented a limited range of controls with no phone, email, web service or web interaction or scripting.

If you add a form lock don’t forget the code. But it is stored unencrypted on the app db so not very secure!

It runs on all the (Limited Number) of version 4.4.2 and above phones and tablets I have been able to try it on. It is more usable on a tablet. 


See 

http://youtu.be/rDtMhRixCEA

For basic instructions.

