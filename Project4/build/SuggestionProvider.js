function SuggestionProvider() {
    //any initializations needed go here
}

SuggestionProvider.prototype.requestSuggestions = function (oAutoSuggestControl, bTypeAhead) {



    //determine suggestions for the control
    var xhttp = new XMLHttpRequest();
    var val = oAutoSuggestControl.textbox.value;
    var request = "suggest?query=" + encodeURI(val);

    //display suggestions for debugging
    //


    xhttp.open("GET", request);
    xhttp.onreadystatechange = function() {

        //var htmlCode = "<ul>";
        var aSuggestions = new Array();
    	if (xhttp.readyState == 4) {
            var response = xhttp.responseXML;

    		var s = response.getElementsByTagName("CompleteSuggestion");

    		for (var i = 0; i < s.length; i++) {
                var text = s[i].childNodes[0].getAttribute("data");
                if (text.indexOf(val) == 0) {
                    aSuggestions.push(text);
                    //htmlCode += "<li><b>" + aSuggestions[i] + "</b></li>";
                }
    		}

    	}

        //htmlCode += "</ul>";
        //document.getElementById("suggestion").innerHTML = "" + htmlCode;

        oAutoSuggestControl.autosuggest(aSuggestions, bTypeAhead);
    }

    xhttp.send(null);

};

