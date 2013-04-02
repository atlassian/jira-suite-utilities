(function($) {

    var global = window,
        JSONP_CALLBACK_NAME = "onJSUGMapsLoad",
        mapsCallbacks = [],
        googleLoaderRequested = false,
        googleMapsRequested = false;

    function runMapsCallbacks() {
        var cb;
        while (cb = mapsCallbacks.shift()) {
            cb();
        }
    }

    function loadMapAndRunCallbacks(callback) {
        var isGoogleMapsPresent = global.google.maps && global.google.maps.LatLng;

        if (isGoogleMapsPresent) {
            //Google Maps is here, run all the pending callbacks
            mapsCallbacks.push(callback);
            runMapsCallbacks();

        } else if (googleMapsRequested) {
            //Google Maps is not present but has been requested -> Add the callback to the maps queue,
            //it will be eventually executed by loadMapAndRunCallbacks()
            mapsCallbacks.push(callback);

        } else {
            //Google Maps is not present nor being requested -> Add the callback to the queue and request it
            googleMapsRequested = true;
            mapsCallbacks.push(callback);
            google.load("maps","3", {other_params:'sensor=false', callback: runMapsCallbacks} );
        }
    }

    function runWithGoogleMaps(callback) {
        if (global.google && global.google.load) {
            //Google Loader is present -> Load GMaps and run the callback
            loadMapAndRunCallbacks(callback);

        } else if (googleLoaderRequested) {
            //Google Loader is not present but has been requested -> Add the callback to the maps queue,
            //it will be eventually executed by loadMapAndRunCallbacks()
            mapsCallbacks.push(callback);

        } else {
            // Google Loader is not present nor requested -> Request it and add the callback to the maps queue
            googleLoaderRequested = true;
            global[JSONP_CALLBACK_NAME] = function() {
                loadMapAndRunCallbacks(callback);
            };
            var script = document.createElement("script");
            script.src = "https://www.google.com/jsapi?callback="+JSONP_CALLBACK_NAME;
            script.type = "text/javascript";
            $("body").append(script);
        }
    }


    function loadAndDisplayLocationOnMap(location, callback) {
        runWithGoogleMaps(function() {
            new google.maps.Geocoder().geocode({
                address: location
            }, callback);
        });
    }

    function toggleMap(jsuMapField) {
        if (jsuMapField.hasClass("open") || jsuMapField.hasClass("notFound")) {
            hideMap(jsuMapField);
        } else {
            displayMap(jsuMapField)
        }
    }

    function hideMap(jsuMapField) {
        jsuMapField.removeClass("open");
        jsuMapField.removeClass("notFound");
    }

    function displayMap(jsuMapField) {
        var location = jsuMapField.find("a.location").text(),
            mapDiv = jsuMapField.find(".jsuMap");

        loadAndDisplayLocationOnMap(location, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                jsuMapField.addClass("open");
                var map = new google.maps.Map(mapDiv.get(0), {
                    zoom: 8,
                    center: results[0].geometry.location,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                });

                new google.maps.Marker({
                    map: map,
                    position: results[0].geometry.location,
                    title: location
                });
            } else {
                jsuMapField.addClass("notFound");
            }
        });
    }

    JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, context, reason) {
        if (reason === JIRA.CONTENT_ADDED_REASON.panelRefreshed || reason === JIRA.CONTENT_ADDED_REASON.pageLoad) {
            var jsuMapFields = $(".jsuLocation.ready", context);
            jsuMapFields.removeClass("ready");
            jsuMapFields.each(function(i, jsuMapField) {
                jsuMapField = $(jsuMapField);
                jsuMapField.find("> a").click(function(e) {
                    e.preventDefault();
                    toggleMap(jsuMapField);
                })
            });
        }
    });

    JIRA.bind(JIRA.Events.INLINE_EDIT_STARTED, function() {
        JIRA.Issues.InlineEdit.BlurTriggerMapping.custom.locationselect = JIRA.Issues.InlineEdit.BlurTriggers.Default;
        JIRA.Issues.InlineEdit.BlurTriggerMapping.custom.locationtextfield = JIRA.Issues.InlineEdit.BlurTriggers.Default;
    });
    /*

    function addAddressToMap_${customField.id}(results, status) {
     field = '${customField.id}';

     if (status == google.maps.GeocoderStatus.OK) {
     var mapDiv = document.getElementById(field + "-map");
     var buttonDiv = document.getElementById(field + "-hide-button");

     mapDiv.style.display = "";
     buttonDiv.style.display = "";

     if (mapDiv.getAttribute("gmapLoaded") == null) {
     var map = new google.maps.Map(mapDiv, {
     zoom: 8,
     center: results[0].geometry.location,
     mapTypeId: google.maps.MapTypeId.ROADMAP
     });

     var marker = new google.maps.Marker({
     position: results[0].geometry.location,
     map: map,
     title: "$!value.toString()"
     });

     mapDiv.setAttribute("gmapLoaded", "loaded");
     }
     } else {
     var label = document.getElementById(field + "-not-found");

     label.style.display = "";
     }
     }
     */
})(AJS.$);