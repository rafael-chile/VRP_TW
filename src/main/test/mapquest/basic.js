var myObject = {
    firstName:"John",
    lastName: "Doe",
    fullName: function() {
        return this.firstName + " " + this.lastName;
    }
}
document.getElementById("demo").innerHTML = myObject.fullName(); // JavaScript Document