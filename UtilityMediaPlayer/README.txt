Utility Media Player (UMP)
------------

1. Authors
Abel Keith
Danny Miles
Kaius Reed
Andrew Su

-------------

2. Compilation 

TODO TODO TODO TODO TODO

Note that normaltest.mp3 seems to display the wrong duration. This is apparently
an error in the metadata of the song, as it is reproduced when opening it in other
media players, and the UMP correctly displays the length of the other test audio.
---------------

3. Use Instructions

   a. General Use
	
	-Opening a file
	   Double-click on a file in the library, OR click File -> Open File and select a file.
	   This will open the file in its proper player, and begin play.
	-Playing a file
	   Files will play upon being opened. If not, or if they have been paused,
	   press the play/pause button (the center button of the main window).
	-Pausing a file
	   While playing, press the play/pause button (the center button of the main window).
	-Volume control
	   The volume can be adjusted by moving in the slider on the bottom-right
	   corner of the window. Volume will remain consistent even when opening different
	   files.
	-Skipping tracks
	   Pressing the back or forward buttons (the arrows on the main window) will 
	   advance through the library as expected, opening the next or previous file.
	-Import a file
	   Click File -> Import File to Library to add a file from your file system to
	   UMP's library. It will then appear on the left sidebar, even after restarting
	   the application. Note that if the file is later moved, it will have to be
	   re-imported to the UMP.

   b. Image Viewer
	-Image Transforms
	  Clicking Image -> Transform opens a menu that allows you to rotate the image
	  in 90 degree intervals, as well as flip it horizontally or vertically.
	-Properties
	  Clicking Image -> Image Properties opens a dialog box that lists the path
	  to this image, as well as its dimensions.
	

   c. Music Player
	-Track Seeking
	  The slider to the right of the time controls playback of the track
	  Adjusting it will cause the track to move playback to the selected value.

   d. Video Player
	-Seeking
	  The slider below the video and to the left of the timestamp will control
	  playback. Adjusting it will move the video to the selected value.
	-Snapshot
	  Clicking Video -> Snapshot saves the current frame of the video 
	  as an image. Saved in the application's output folder.
	-Video Clipping
	  Click Video -> Clip Video. Then, enter the duration of the clip you want
	  to make, using the start time and then the end time. The output clip will
	  be saved in the application's output folder.
	
	

   e. Playlists


   f. File Conversion
	-GIF to Video 
	  Click Image -> Convert GIF to Video. The new video will be saved in the 
	  application's output folder.
	-Video to Audio
	  Click Video -> Extract Audio Track. You can then choose whether to save 
	  the entire video as an audio file, or clip. In the latter case, you will 
	  then select the desired start and end times of the clip. The final audio will
	  be saved in the application's output folder.
	-Video to GIF
	  Click Video -> Convert to GIF. Select the desired start and end times of the GIF,
	  and the video will be clipped appropriately. The GIF will be saved in the application's
	  output folder. 
	  
	

 