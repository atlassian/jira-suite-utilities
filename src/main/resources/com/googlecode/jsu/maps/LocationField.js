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

    function isNumber(n) {
        return !isNaN(parseFloat(n)) && isFinite(n);
    }

    function isEmpty(str) {
        return (!str || 0 === str.length);
    }

    function displayMap(jsuMapField) {
        var location = jsuMapField.find("a.location").text(),
            destination = "",
            mapDiv = jsuMapField.find(".jsuMap");

        var dirNames = location.split("=>");
        if(dirNames.length==2) {
            location = dirNames[0].trim();
            destination = dirNames[1].trim();
        }

        loadAndDisplayLocationOnMap(location, function(results, status) {
            if (status == google.maps.GeocoderStatus.OK) {
                jsuMapField.addClass("open");
                var map = new google.maps.Map(mapDiv.get(0), {
                    zoom: 8,
                    center: results[0].geometry.location,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                });

                /* did we get GPS coordinates as location, then point marker to it instead of geocoded location */
                var coords = location.split(",");
                if(coords.length==2 && isNumber(coords[0]) && isNumber(coords[1])) {
                    var latlng = new google.maps.LatLng(coords[0],coords[1]);
                    new google.maps.Marker({
                        position: latlng,
                        map: map,
                        title: location
                    });
                } else {
                    new google.maps.Marker({
                        map: map,
                        position: results[0].geometry.location,
                        title: location
                    });
                }

                if(!isEmpty(destination)) {
                    var directionsDisplay = new google.maps.DirectionsRenderer();
                    var directionsService = new google.maps.DirectionsService();

                    directionsDisplay.setMap(map);

                    var request = {
                        origin:location,
                        destination:destination,
                        travelMode: google.maps.TravelMode.DRIVING
                    };
                    directionsService.route(request, function(response, status) {
                        if (status == google.maps.DirectionsStatus.OK) {
                            directionsDisplay.setDirections(response);
                        }
                    });
                }
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
        JIRA.Issues.InlineEdit.BlurTriggerMapping.custom.directionsfield = JIRA.Issues.InlineEdit.BlurTriggers.Default;
    });
})(AJS.$);