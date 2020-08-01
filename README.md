# Android Address Book
## Installation
Clone the repository and load in Android Studio

``git clone https://github.com/jamesscully/Phonebook``


## Features
**Adding / Viewing a contact** - Screens 0.2 and 0.4 have been combined, whereby changing the `state` variable in the ViewModel will update the UI appropriately.

**Unsaved changes** - Users are notified if they are leaving unsaved changes upon attempting to leave the (edit/create) activity.

**Search functionality** - Users can search for a given name, email or phone number.

**Input validation** - Only emails and phone numbers are validated through the use of Regex. Upon an error, the user is unable to save or update the contact unless they wish to discard changes.

## Technologies Used
- Kotlin (+ Coroutines)
- RxJava + RxBindings
- Room
- JUnit 4

## Tests
[Instrumented Test - Database](app/src/androidTest/java/com/scullyapps/phonebook/ContactDatabaseTest.kt)

[Unit Test - Inputs](app/src/test/java/com/scullyapps/phonebook/ContactValidationTest.kt)

## Devices Tested
There was no specification on the target/min API, so I've targetted 29 and 19 respectively.
Tested on devices: 

- (Physical) OnePlus 6 running Oreo 
- (Emulator) Pixel 2 API 29, Galaxy Nexus Lollipop API 22

## Notes
**Editing / Deletion of Contact** - I've implemented editing / deletion of a contact, primarily as I wrote the code for CRUD whilst initially testing and writing the database. Edit mode can be exited using the back-button.

**Menu Items** - Menu Items aren't hidden or updated in the correct contexts - i.e. edit icon doesn't change once you enter edit mode, but I've left this alone to avoid writing extra code.

Have fun!