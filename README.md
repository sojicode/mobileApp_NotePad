## Mobile Application - NotePad

Uses: RecyclerView, Multi-Activity, JSON File, Option-Menus

### Requirements

* This app allows the creation and maintenance of multiple notes. Any number of notes are allowed (including no
notes at all). Notes are made up of a title, a note text, and a last-update time.

* There is no need to use a different layout for landscape orientation in this application. One layout must work
fine in either orientation.

* Notes should be saved to (and loaded from) the internal file system in JSON format. If no file is found upon loading, the application should start with no existing notes and no errors. (A new JSON file would then be created when new notes are saved).

* JSON file loading must happen in the onCreate method. Saving should happen in the onPause method.

* A simple java Note class (with title, note text, and last save date) should be created to represent each individual
note in the application.
