var myObject2 = {
    firstName:"basic2",
    lastName: "entrando...",
    fullName: function() {
        return this.firstName + " " + this.lastName;
    }
}
document.getElementById("demo2").innerHTML = myObject2.fullName(); 

// https://developer.mapquest.com/sites/default/files/mapquest/api-docs/doc/js/samples/directions/basic.js

var SAMPLE_POST = HOST_URL + '/directions/v2/route?key=pWVuV1gQyrHTRys07TGATGD9vJO7udqM&from=Lancaster,PA&to=York,PA&callback=renderNarrative';

function showBasicURL() {
    var safe = SAMPLE_POST;
    document.getElementById('basicSampleUrl').innerHTML = safe.replace(/</g, '&lt;').replace(/>/g, '&gt;');
};
//document.getElementById("basicSampleUrl").innerHTML = 'HOST_URL' + ' HOST_URL';

function doClick() {
	document.getElementById('narrative').innerHTML = 'Pending...';
    var script = document.createElement('script');
    script.type = 'text/javascript';
    showBasicURL();
    var newURL = SAMPLE_POST.replace('pWVuV1gQyrHTRys07TGATGD9vJO7udqM', APP_KEY);
    script.src = newURL;
    document.body.appendChild(script);
};

function renderNarrative(response) {
    var legs = response.route.legs;
    var html = '';
    var i = 0;
    var j = 0;
    var trek;
    var maneuver;
    
    html += '<table><tr><th colspan=3>Narrative</th></tr><tbody>'
    
    for (; i < legs.length; i++) {
        for (j = 0; j < legs[i].maneuvers.length; j++) {
            maneuver = legs[i].maneuvers[j];
            
            html += '<tr>';
            html += '<td>&nbsp;';
            if (maneuver.iconUrl) {
                html += '<img src="' + maneuver.iconUrl + '">  '; 
            } 
            for (k = 0; k < maneuver.signs.length; k++) {
                var sign = maneuver.signs[k];
                if (sign && sign.url) {
                    html += '<img src="' + sign.url + '">  '; 
                }
            }
            
            html += '</td>'
            html += '<td>' + maneuver.narrative + '</td>'
            html += '<td>'
            if (maneuver.mapUrl) {
                html += '<img src="' + maneuver.mapUrl + '">';
            } else {
                html += '&nbsp;'
            }
            html += '</td>'
            html += '</tr>';
        }
    }
    
    html += '</tbody></table>';
    
    document.getElementById('narrative').style.display = "";
    document.getElementById('narrative').innerHTML = html;
}