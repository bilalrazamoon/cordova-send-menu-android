#org.cordova.sendmenu

SendMenu
========

> The `SendMenu` plugin to retrieve data from send menu action

Supported Platforms
-------------------

- Android

##Params

    sendmenu.ACTION_SEND
    sendmenu.ACTION_VIEW
    sendmenu.EXTRA_TEXT
    sendmenu.EXTRA_SUBJECT
    sendmenu.EXTRA_STREAM
    sendmenu.EXTRA_EMAIL
    sendmenu.ACTION_CALL
    sendmenu.ACTION_SENDTO

##window.plugins.sendmenu.hasExtra

    window.plugins.sendmenu.hasExtra(params, success, fail)

### Sample code

    window.plugins.sendmenu.hasExtra(sendmenu.EXTRA_STREAM,
        function(val) {
            // val is the value of EXTRA_TEXT
        }, function() {
            // There was no extra supplied.
        }
    );


##window.plugins.sendmenu.getExtra

    window.plugins.sendmenu.getExtra(params, success, fail)

### Sample code

    window.plugins.sendmenu.getExtra(sendmenu.EXTRA_STREAM,
        function(url) {
            // url is the value of EXTRA_TEXT
        }, function() {
            // There was no extra supplied.
        }
    );

###ScreenShots
!screenshots/device-1.jpg.gif|alt="send menu"!
!screenshots/device-2.jpg.gif|alt="image show"!

###Copyright Bilal Raza @hafizbilal112
