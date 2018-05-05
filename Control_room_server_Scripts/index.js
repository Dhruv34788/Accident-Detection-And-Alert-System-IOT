const mapStyle = [
  {
    elementType: 'geometry',
    stylers: [
      {
        color: '#eceff1'
      }
    ]
  },
  {
    elementType: 'labels',
    stylers: [
      {
        visibility: 'off'
      }
    ]
  },
  {
    featureType: 'administrative',
    elementType: 'labels',
    stylers: [
      {
        visibility: 'on'
      }
    ]
  },
  {
    featureType: 'road',
    elementType: 'geometry',
    stylers: [
      {
        color: '#cfd8dc'
      }
    ]
  },
  {
    featureType: 'road',
    elementType: 'geometry.stroke',
    stylers: [
      {
        visibility: 'off'
      }
    ]
  },
  {
    featureType: 'road.local',
    stylers: [
      {
        visibility: 'off'
      }
    ]
  },
  {
    featureType: 'water',
    stylers: [
      {
        color: '#b0bec5'
      }
    ]
  }
];
 
  var database = firebase.database();
  var dB = database.ref();
  
  function initMap() {
  var latlng = new google.maps.LatLng(23.18,72.62);
  map = new google.maps.Map(document.getElementById('map'), {
  center: latlng,
  zoom:12,
});


    dB.child("users").once("value", function(snapshot){
    
      snapshot.forEach(userSnapshot => {
      var pos = { lat : userSnapshot.val().latitude , lng: userSnapshot.val().longitude};
      var name = userSnapshot.val().userId;
      addVolunteer(pos,name,map);
    })
    }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });

    dB.child("vehicles").once("value", function(snapshot){
    
      snapshot.forEach(userSnapshot => {
      var pos = { lat : userSnapshot.val().latitude , lng: userSnapshot.val().longitude};
      var name = userSnapshot.val().userId;
      addVehicle(pos,name,map);
    })
    }, function (errorObject) {
    console.log("The read failed: " + errorObject.code);
  });


  dB.child("accidents").on("value", function(snapshot) {
    snapshot.forEach(userSnapshot => {
    var status = userSnapshot.val().status;
    var vehicleID = userSnapshot.val().vehicleID;
    if(status == -1){
      console.log("finding help for "+vehicleID);
      addAccident(vehicleID,map);
      findVolunteer(vehicleID);
    }
    else if(status == 0){
      addHault(vehicleID,map);
    }
  })
  }, function (errorObject) {
  console.log("The read failed: " + errorObject.code);
});
}

function addVolunteer(pos,na,ma){
   var marker = new google.maps.Marker({
          position: pos,
          title: na,
          map: ma,
          icon : 'http://maps.google.com/mapfiles/ms/icons/green-dot.png'
  });
}




function addVehicle(pos,na,ma){
   var marker = new google.maps.Marker({
          position: pos,
          title: na,
          map: ma,
          icon : 'http://maps.google.com/mapfiles/ms/icons/yellow-dot.png'
  });
}
function addAccident(vehicleID,ma){
  
  dB.child("vehicles").child(vehicleID).on("value" , function(snapshot){
    var pos = { lat : snapshot.val().latitude , lng: snapshot.val().longitude};
    var name = snapshot.val().username;
    var status = snapshot.val.status;
    var marker = new google.maps.Marker({
          position: pos ,
          title: name,
          map: ma,
        });
});
}
function addHault(vehicleID,ma){
  
  dB.child("vehicles").child(vehicleID).on("value" , function(snapshot){
    var pos = { lat : snapshot.val().latitude , lng: snapshot.val().longitude};
    var name = snapshot.val().username;
    var status = snapshot.val.status;
    var marker = new google.maps.Marker({
          position: pos ,
          title: name,
          map: ma,
          icon : 'http://maps.google.com/mapfiles/ms/icons/blue-dot.png'
        });
});
}

function findVolunteer(vehicleID){
  dB.child("vehicles").child(vehicleID).once("value" , function(snapshots){
    console.log("finding helper for "+vehicleID);
    var lats = snapshots.val().latitude;
    var longs = snapshots.val().longitude;
    var dist = 50000000;
    var TuserID ;
    dB.child("users").once("value", function(snapshot) {
  
      snapshot.forEach(userSnapshot => {
        if(userSnapshot.val().status == 1){    
          var lat =  userSnapshot.val().latitude 
          var long = userSnapshot.val().longitude;
          var temp_dist = Math.sqrt( Math.pow(Math.abs(lats-lat),2) + Math.pow(Math.abs(longs-long),2) ) ; 
          var userId = userSnapshot.val().userId;
          console.log("distance between "+userId+" and "+vehicleID+" is "+temp_dist);
          if(temp_dist < dist){
            TuserID = userSnapshot.val().userId;
            dist = temp_dist;
          }
        }
      });
      if(TuserID != null){
      console.log(TuserID+" choosen for "+vehicleID);
      dB.child("users").child(TuserID).once("value",function(snapshot){
        dB.child("users").child(TuserID).child("vehicleID").set(vehicleID);
        dB.child("accidents").child(vehicleID).child("status").set(0);
      });
      }
    });
  });
}



