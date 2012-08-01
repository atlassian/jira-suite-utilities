(function($) {

    function ensureGoogleMapsIsLoaded(callback) {
        if (typeof google !== 'undefined' && typeof google.maps !== "undefined") {
            callback();
        } else {
            window.onGMapsLoad = callback;
            $("body").append($("<script src='https://maps-api-ssl.google.com/maps/api/js?v=3&sensor=false&callback=onGMapsLoad'></script>"));
        }
    }

    function loadAndDisplayLocationOnMap(location, callback) {
        ensureGoogleMapsIsLoaded(function() {
            window.onGMapsLoad = null;
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

    JIRA.bind(JIRA.Events.NEW_CONTENT_ADDED, function (e, context) {
        var jsuMapFields = $(".jsuLocation.ready", context);
        jsuMapFields.removeClass("ready");
        jsuMapFields.each(function(i, jsuMapField) {
            jsuMapField = $(jsuMapField);
            jsuMapField.find("> a").click(function(e) {
                e.preventDefault();
                toggleMap(jsuMapField);
            })
        });
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