/**
 * cordova sendmenu plugin
 *
 */
 (function(cordova){
    var SendMenu = function() {

    };

    SendMenu.prototype.ACTION_SEND = "android.intent.action.SEND";
    SendMenu.prototype.ACTION_VIEW= "android.intent.action.VIEW";
    SendMenu.prototype.EXTRA_TEXT = "android.intent.extra.TEXT";
    SendMenu.prototype.EXTRA_SUBJECT = "android.intent.extra.SUBJECT";
    SendMenu.prototype.EXTRA_STREAM = "android.intent.extra.STREAM";
    SendMenu.prototype.EXTRA_EMAIL = "android.intent.extra.EMAIL";
    SendMenu.prototype.ACTION_CALL = "android.intent.action.CALL";
    SendMenu.prototype.ACTION_SENDTO = "android.intent.action.SENDTO";

    SendMenu.prototype.hasExtra = function(params, success, fail) {
        return cordova.exec(function(args) {
            success(args);
        }, function(args) {
            fail(args);
        }, 'SendMenu', 'hasExtra', [params]);
    };

    SendMenu.prototype.getExtra = function(params, success, fail) {
        return cordova.exec(function(args) {
            success(args);
        }, function(args) {
            fail(args);
        }, 'SendMenu', 'getExtra', [params]);
    };

    window.sendmenu = new SendMenu();
    
    // backwards compatibility
    window.plugins = window.plugins || {};
    window.plugins.sendmenu = window.sendmenu;
})(window.PhoneGap || window.Cordova || window.cordova);
