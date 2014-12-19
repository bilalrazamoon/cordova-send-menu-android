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
![ScreenShot](https://raw.github.com/hafizbilal112/cordova-send-menu-android/master/screenshots/device-1.jpg)
![ScreenShot](https://raw.github.com/hafizbilal112/cordova-send-menu-android/master/screenshots/device-2.jpg)

###Copyright Bilal Raza @hafizbilal112
